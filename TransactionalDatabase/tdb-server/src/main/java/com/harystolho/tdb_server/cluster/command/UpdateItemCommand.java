package com.harystolho.tdb_server.cluster.command;

import java.util.HashMap;
import java.util.Map;

import com.harystolho.tdb_server.cluster.Cluster;
import com.harystolho.tdb_server.cluster.Item;
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

	public LogBlock toLogBlock() {
		return null; // TODO implement
	}

	@Override
	public QueryResult execute(Cluster cluster) {
		return cluster.handle(this);
	}

	@Override
	public Command<?> undo(LogBlock logBlock) {
		return null;
	}

}
