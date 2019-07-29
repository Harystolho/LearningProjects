package com.harystolho.tdb_server.cluster;

import java.util.LinkedList;
import java.util.List;

import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.transaction.CommandLogger;
import com.harystolho.tdb_shared.QueryResult;

public class Cluster {

	private final String name;
	private final List<Item> items;

	private final CommandLogger commandLogger;

	public Cluster(String name, CommandLogger logger) {
		this.name = name;
		this.items = new LinkedList<>();

		commandLogger = logger;
	}

	public QueryResult handle(InsertItemCommand iic) {
		commandLogger.log(iic.toLogBlock());

		insertItem(Item.fromMap(iic.getValues()));

		return new QueryResult();
	}

	private void insertItem(Item item) {
		items.add(item);
	}

}