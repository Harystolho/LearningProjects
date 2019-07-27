package com.harystolho.tdb_server.cluster.command;

import com.harystolho.tdb_server.cluster.Cluster;
import com.harystolho.tdb_server.command.Command;

public abstract class ClusterCommand implements Command<Cluster> {

	protected String cluster;

	public ClusterCommand(String cluster) {
		this.cluster = cluster;
	}

	public String getClusterName() {
		return cluster;
	}

	@Override
	public Class<?> getHandlerClassType() {
		return ClusterCommand.class;
	}

}
