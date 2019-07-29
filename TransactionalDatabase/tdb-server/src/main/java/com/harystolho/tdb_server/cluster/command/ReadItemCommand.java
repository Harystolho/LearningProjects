package com.harystolho.tdb_server.cluster.command;

import com.harystolho.tdb_server.cluster.Cluster;
import com.harystolho.tdb_shared.QueryResult;

public class ReadItemCommand extends ClusterCommand {

	public ReadItemCommand(String cluster) {
		super(cluster);
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
