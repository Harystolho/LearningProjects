package com.harystolho.tdb_server.cluster;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.harystolho.tdb_server.cluster.command.DeleteItemCommand;
import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.cluster.command.ReadItemCommand;
import com.harystolho.tdb_server.cluster.command.UpdateItemCommand;
import com.harystolho.tdb_server.cluster.query.Query;
import com.harystolho.tdb_server.transaction.CommandLogger;
import com.harystolho.tdb_shared.QueryResult;

public class Cluster { // TODO save cluster to file

	private final String name;
	private final List<Item> items;

	private final CommandLogger commandLogger;

	public Cluster(String name, List<Item> items, CommandLogger logger) {
		this.name = name;
		this.items = items;

		this.commandLogger = logger;
	}
 
	public Cluster(String name, CommandLogger logger) {
		this(name, new LinkedList<>(), logger);
	}

	public QueryResult handle(InsertItemCommand iic) {
		commandLogger.log(iic.toLogBlock());

		insertItem(Item.fromMap(iic.getValues()));

		return QueryResult.EMPTY;
	}

	public QueryResult handle(DeleteItemCommand dic) {
		commandLogger.log(dic.toLogBlock());

		removeItemsThatMatchQuery(dic.getQuery());

		return QueryResult.EMPTY;
	}

	public QueryResult handle(UpdateItemCommand uic) {
		commandLogger.log(uic.toLogBlock());

		findItemsThatMatchQuery(uic.getQuery()).forEach((item) -> {
			int idx = items.indexOf(item);

			items.set(idx, item.merge(Item.fromMap(uic.getNewValues())));
		});

		return QueryResult.EMPTY;
	}

	public QueryResult handle(ReadItemCommand ric) {
		List<Map<String, String>> matchingItems = findItemsThatMatchQuery(ric.getQuery()).stream().map(Item::toMap)
				.collect(Collectors.toList());

		QueryResult result = new QueryResult();
		result.put("items", matchingItems);

		return result;
	}

	private List<Item> findItemsThatMatchQuery(Query<Item> query) {
		return items.stream().filter(query::isSatisfiedBy).collect(Collectors.toList());
	}

	private void removeItemsThatMatchQuery(Query<Item> query) {
		findItemsThatMatchQuery(query).forEach(this::removeItem);
	}

	private void insertItem(Item item) {
		items.add(item);
	}

	private void removeItem(Item item) {
		items.remove(item);
	}

}
