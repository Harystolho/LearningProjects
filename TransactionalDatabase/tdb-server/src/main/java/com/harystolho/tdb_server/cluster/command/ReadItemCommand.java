package com.harystolho.tdb_server.cluster.command;

import com.harystolho.tdb_server.cluster.Cluster;
import com.harystolho.tdb_server.cluster.Item;
import com.harystolho.tdb_server.cluster.command.query.Query;
import com.harystolho.tdb_shared.QueryResult;

public class ReadItemCommand extends ClusterCommand {

	private final Query<Item> query;

	public ReadItemCommand(String cluster, Query<Item> query) {
		super(cluster);
		this.query = query;
	}

	public Query<Item> getQuery() {
		return query;
	}

	@Override
	public QueryResult execute(Cluster cluster) {
		return cluster.handle(this);
	}

	@Override
	public QueryResult undo(Cluster t) {
		return null;
	}

}
