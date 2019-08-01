package com.harystolho.tdb_server.cluster.command;

import java.util.HashMap;
import java.util.Map;

import com.harystolho.tdb_server.cluster.Cluster;
import com.harystolho.tdb_server.cluster.query.ItemFieldQuery;
import com.harystolho.tdb_server.command.Command;
import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_shared.QueryResult;

public class InsertItemCommand extends TransactionalClusterCommand {

	private Map<String, String> values;

	public InsertItemCommand(long transactionId, String cluster, Map<String, String> values) {
		super(cluster, transactionId);
		this.values = values;
	}

	public Map<String, String> getValues() {
		return new HashMap<String, String>(values);
	}

	@Override
	public QueryResult execute(Cluster cluster) {
		return cluster.handle(this);
	}

	public LogBlock toLogBlock(long itemId) {
		Map<String, String> valuesToSave = new HashMap<>();
		valuesToSave.put("_cluster", this.getClusterName());
		valuesToSave.put("id", String.valueOf(itemId));

		return new LogBlock(transactionId, "INSERT_ITEM", valuesToSave);
	}

	@SuppressWarnings("unchecked")
	public static DeleteItemCommand undo(LogBlock logBlock) {
		Map<String, String> values = (Map<String, String>) logBlock.getObject();

		String cluster = values.remove("_cluster");

		return new DeleteItemCommand(NO_TRANSACTION, cluster, ItemFieldQuery.equal("_id", values.get("id")));
	}

}
