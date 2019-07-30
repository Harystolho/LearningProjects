package com.harystolho.tdb_server.cluster;

import java.util.HashMap;
import java.util.Map;

public final class Item {

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

	public Map<String, String> toMap() {
		return new HashMap<String, String>(fields);
	}

	/**
	 * @param merger
	 * @return a new {@link Item} that has the fields present in the {merger}. If
	 *         the field key exists in this item, it's replaced, if it doesn't, it
	 *         is added to this item
	 */
	public Item merge(Item merger) {
		Map<String, String> copy = new HashMap<String, String>(fields);
		copy.putAll(merger.fields);

		return Item.fromMap(copy);
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
