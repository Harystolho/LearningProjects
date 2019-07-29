package com.harystolho.tdb_server.cluster;

import java.util.HashMap;
import java.util.Map;

import com.harystolho.tdb_server.cluster.command.ClusterCommand;
import com.harystolho.tdb_server.command.CommandDispatcher;
import com.harystolho.tdb_server.command.CommandHandler;
import com.harystolho.tdb_server.transaction.CommandLogger;
import com.harystolho.tdb_shared.QueryResult;

public class ClusterCatalog implements CommandHandler<ClusterCommand> {

	private final CommandLogger commandLogger;

	private final Map<String, Cluster> clusters;

	public ClusterCatalog(CommandDispatcher dispatcher, CommandLogger logger) {
		this.commandLogger = logger;
		clusters = new HashMap<>();

		dispatcher.register(ClusterCommand.class, this);
	}

	@Override
	public QueryResult handle(ClusterCommand command) {
		Cluster cluster = getOrCreateCluster(command.getClusterName());

		return command.execute(cluster);
	}

	private Cluster getOrCreateCluster(String clusterName) {
		if (!clusters.containsKey(clusterName))
			createCluster(clusterName);

		return clusters.get(clusterName);
	}

	private void createCluster(String name) {
		clusters.put(name, new Cluster(name, commandLogger));
	}

}
