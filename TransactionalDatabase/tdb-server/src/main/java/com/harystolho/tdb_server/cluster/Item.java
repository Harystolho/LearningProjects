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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Item other = (Item) obj;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		return true;
	}

}
