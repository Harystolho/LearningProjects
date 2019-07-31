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

	public LogBlock toLogBlock() {
		Map<String, String> valuesToSave = new HashMap<>(values);
		valuesToSave.put("_cluster", this.getClusterName());

		return new LogBlock(transactionId, "INSERT_ITEM", valuesToSave);
	}

	@SuppressWarnings("unchecked")
	public static Command<?> undo(LogBlock logBlock) {
		Map<String, String> values = (Map<String, String>) logBlock.getObject();

		String cluster = values.remove("_cluster");

		return new DeleteItemCommand(NO_TRANSACTION, cluster, ItemFieldQuery.fromMap(values));
	}

}
