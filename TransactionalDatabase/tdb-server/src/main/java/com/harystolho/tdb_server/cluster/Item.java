package com.harystolho.tdb_server.cluster;

import java.util.HashMap;
import java.util.Map;

public class Item {

	private final Map<String, String> fields;

	public Item() {
		this(new HashMap<>());
	}

	private Item(Map<String, String> map) {
		fields = map;
	}

	public String get(String key) {
		return fields.get(key);
	}
	
	public static Item fromMap(Map<String, String> map) {
		return new Item(map);
	}

}
