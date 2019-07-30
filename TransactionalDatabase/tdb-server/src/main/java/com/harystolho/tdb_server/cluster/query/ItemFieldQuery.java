package com.harystolho.tdb_server.cluster.query;

import java.util.Map;

import com.harystolho.tdb_server.cluster.Item;

public class ItemFieldQuery {

	public static Query<Item> equal(String fieldName, String value) {
		return Query.make(item -> value.equals(item.get(fieldName)));
	}

	public static Query<Item> less(String fieldName, long comparator) {
		return Query.make((item) -> {
			try {
				long value = Long.valueOf(item.get(fieldName));

				return value < comparator;
			} catch (Exception e) {
				return false;
			}
		});
	}

	public static Query<Item> greater(String fieldName, long comparator) {
		return Query.make((item) -> {
			try {
				long value = Long.valueOf(item.get(fieldName));

				return value > comparator;
			} catch (Exception e) {
				return false;
			}
		});
	}

	/**
	 * Creates a query from a map. Each map entry is converted in a
	 * {@link ItemFieldQuery}. If more than 1 entry is present in the map, the
	 * queries are joined together using {@link Query#and(Query)}
	 * 
	 * @param map
	 * @return
	 */
	public static Query<Item> fromMap(Map<String, String> map) {
		return map.entrySet().stream().map((entry) -> ItemFieldQuery.equal(entry.getKey(), entry.getValue()))
				.reduce(Query::and).get();
	}

}
