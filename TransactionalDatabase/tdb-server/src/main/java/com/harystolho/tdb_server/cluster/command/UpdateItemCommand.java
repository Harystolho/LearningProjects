package com.harystolho.tdb_server.cluster.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.harystolho.tdb_server.cluster.Cluster;
import com.harystolho.tdb_server.cluster.Item;
import com.harystolho.tdb_server.cluster.query.ItemFieldQuery;
import com.harystolho.tdb_server.cluster.query.Query;
import com.harystolho.tdb_server.command.Command;
import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_shared.QueryResult;

public class UpdateItemCommand extends TransactionalClusterCommand {

	private Query<Item> query;
	private Map<String, String> newValues;

	public UpdateItemCommand(long transactionId, String cluster, Query<Item> query, Map<String, String> newValues) {
		super(cluster, transactionId);
		this.query = query;
		this.newValues = newValues;
	}

	public Query<Item> getQuery() {
		return query;
	}

	public Map<String, String> getNewValues() {
		return new HashMap<String, String>(newValues);
	}

	@Override
	public QueryResult execute(Cluster cluster) {
		return cluster.handle(this);
	}

	public LogBlock toLogBlock(List<Map<String, String>> updatedItems) {
		Map<String, Object> valuesToSave = new HashMap<>();

		valuesToSave.put("_cluster", this.getClusterName());
		valuesToSave.put("items", updatedItems);

		return new LogBlock(transactionId, "UPDATE_ITEM", valuesToSave);
	}

	@SuppressWarnings("unchecked")
	public static List<Command<?>> undo(LogBlock logBlock) {
		Map<String, Object> values = (Map<String, Object>) logBlock.getObject();

		String cluster = (String) values.remove("_cluster");

		List<Map<String, String>> items = (List<Map<String, String>>) values.get("items");

		return items.stream().map((item) -> {
			item = new HashMap<>(item); // In case item is not modifiable, create a new map that is

			String itemId = item.remove("_id");

			return new UpdateItemCommand(NO_TRANSACTION, cluster, ItemFieldQuery.equal("_id", itemId), item);
		}).collect(Collectors.toList());
	}

}
