package com.harystolho.tdb_server.cluster;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class Item {

	private static final AtomicLong lastId = new AtomicLong();

	private final long id;
	private final Map<String, String> fields;

	public Item() {
		this(new HashMap<>());
	}

	private Item(Map<String, String> map) {
		this.id = lastId.incrementAndGet();
		fields = map;
	}

	public String get(String key) {
		if (key.equals("_id"))
			return String.valueOf(getId());

		return fields.get(key);
	}

	public long getId() {
		return id;
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

	/**
	 * For all fields present in {other}, return the old field of this object if the
	 * field is also present in the {other}. If {other} contains a field that this
	 * object doesn't contain, it will copy the field name and set its value to
	 * <code>null</code>
	 * 
	 * @param fromMap
	 */
	public Item mergeAndReturnOldFields(Item other) {
		Map<String, String> values = new HashMap<>();

		other.fields.entrySet().stream().forEach((entry) -> {
			String fieldValue = this.fields.get(entry.getKey());

			if (fieldValue != entry.getValue()) {
				values.put(entry.getKey(), fieldValue);
			}
		});

		return Item.fromMap(values);
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
