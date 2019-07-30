package com.harystolho.tdb_server.cluster.command;

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

	public LogBlock toLogBlock() {
		return null;
	}

	@Override
	public QueryResult execute(Cluster cluster) {
		return cluster.handle(this);
	}

	@Override
	public QueryResult undo(Cluster cluster) {
		return null;
	}

}
