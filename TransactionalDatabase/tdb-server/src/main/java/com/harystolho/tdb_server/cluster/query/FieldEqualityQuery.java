package com.harystolho.tdb_server.cluster.query;

import com.harystolho.tdb_server.cluster.Item;

public class FieldEqualityQuery extends Query<Item> {

	private final String fieldName;
	private final String value;

	private FieldEqualityQuery(String fieldName, String value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	public static FieldEqualityQuery of(String fieldName, String value) {
		return new FieldEqualityQuery(fieldName, value);
	}

	@Override
	public boolean isSatisfiedBy(Item item) {
		return value.equals(item.get(fieldName));
	}

}
