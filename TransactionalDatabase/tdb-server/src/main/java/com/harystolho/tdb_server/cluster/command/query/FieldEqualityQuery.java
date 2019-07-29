package com.harystolho.tdb_server.cluster.command.query;

import com.harystolho.tdb_server.cluster.Item;

public class FieldEqualityQuery extends Query<Item> {

	private final String fieldName;
	private final String value;

	public FieldEqualityQuery(String fieldName, String value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	@Override
	public boolean isSatisfiedBy(Item item) {
		return value.equals(item.get(fieldName));
	}

}
