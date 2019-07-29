package com.harystolho.tdb_server.cluster.query;

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

}
