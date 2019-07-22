package com.harystolho.tda.shared;

import java.util.HashMap;
import java.util.Map;

public class QueryResult {

	private Map<String, Object> values;

	public QueryResult() {
		values = new HashMap<>();
	}

	public void put(String key, Object value) {
		this.values.put(key, value);
	}

	public String getString(String key) {
		return String.valueOf(values.get(key));
	}

	public int getInt(String key) {
		return (int) values.get(key);
	}

	public long getLong(String key) {
		try {
			return (long) values.get(key);
		} catch (Exception e) {
			return -1;
		}
	}

}
