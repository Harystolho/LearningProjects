package com.harystolho.tdb_server.cluster.command;

import java.util.HashMap;
import java.util.Map;

import com.harystolho.tdb_server.cluster.Cluster;
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
	public QueryResult execute(Cluster t) {
		return null;
	}

	@Override
	public QueryResult undo(Cluster t) {
		return null;
	}

}
