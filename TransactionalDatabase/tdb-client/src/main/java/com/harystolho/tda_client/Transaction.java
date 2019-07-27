package com.harystolho.tda_client;

import com.harystolho.tdb_shared.QueryResult;

/**
 * Wrapper object that adds the transaction id to query request by itself.
 * 
 * @author Harystolho
 *
 */
public class Transaction {

	private long id;
	private Connection connection;

	public Transaction(long id, Connection connection) {
		this.id = id;
		this.connection = connection;
	}

	public void commit() {
		execQuery("COMMIT");
	}

	public void rollback() {
		execQuery("ROLLBACK");
	}

	/**
	 * According to the query specification the transaction id, if present, must be
	 * inserted at the beginning of the query surrounded by "'".
	 * 
	 * Example:
	 * 
	 * <pre>
	 * '123' INSERT ...
	 * </pre>
	 * 
	 * @param query
	 */
	public QueryResult execQuery(String query) {
		return connection.execQuery(String.format("'%s' %s", id, query));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Transaction other = (Transaction) obj;
		if (id != other.id)
			return false;

		return true;
	}

}
