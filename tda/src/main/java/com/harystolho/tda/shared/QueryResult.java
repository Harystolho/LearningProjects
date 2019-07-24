package com.harystolho.tda.shared;

import java.util.HashMap;
import java.util.Map;

public class QueryResult {

	public static final QueryResult EMPTY = new QueryResult();
	
	private Map<String, Object> values;

	/**
	 * If some error occurred while executing the query an exception should be set
	 * here. This exception will be thrown on the client side
	 */
	private RuntimeException exception;

	public QueryResult() {
		values = new HashMap<>();
	}

	public QueryResult(RuntimeException exception) {
		this();
		this.exception = exception;
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

	public void setException(RuntimeException exception) {
		this.exception = exception;
	}

	public RuntimeException getException() {
		return exception;
	}

}
