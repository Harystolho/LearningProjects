package com.harystolho.tdb_server.cluster.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.harystolho.tdb_server.cluster.Cluster;
import com.harystolho.tdb_server.cluster.Item;
import com.harystolho.tdb_server.cluster.query.Query;
import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_shared.QueryResult;

public class DeleteItemCommand extends TransactionalClusterCommand {

	private final Query<Item> query;

	public DeleteItemCommand(long transactionId, String cluster, Query<Item> query) {
		super(cluster, transactionId);
		this.query = query;
	}

	public Query<Item> getQuery() {
		return query;
	}

	@Override
	public QueryResult execute(Cluster cluster) {
		return cluster.handle(this);
	}

	public LogBlock toLogBlock(List<Map<String, String>> deletedItems) {
		Map<String, Object> valuesToSave = new HashMap<>();

		valuesToSave.put("_cluster", this.getClusterName());
		valuesToSave.put("items", deletedItems);

		return new LogBlock(transactionId, "DELETE_ITEM", valuesToSave);
	}

	@SuppressWarnings("unchecked")
	public static List<InsertItemCommand> undo(LogBlock logBlock) {
		Map<String, Object> values = (Map<String, Object>) logBlock.getObject();

		String cluster = (String) values.remove("_cluster");

		List<Map<String, String>> itemsToAdd = (List<Map<String, String>>) values.get("items");

		return itemsToAdd.stream().map((item) -> new InsertItemCommand(NO_TRANSACTION, cluster, item))
				.collect(Collectors.toList());
	}

}
