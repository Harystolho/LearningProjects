package com.harystolho.tdb_shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResult implements Serializable {

	private static final long serialVersionUID = 65741258894L;

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

	@SuppressWarnings("rawtypes")
	public List getList(String key) {
		return (List) values.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key, Class<T> type) {
		return (List<T>) values.get(key);
	}

	public void setException(RuntimeException exception) {
		this.exception = exception;
	}

	public RuntimeException getException() {
		return exception;
	}

}
