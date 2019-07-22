package com.harystolho.tda.client;

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
		execQuery("COMMIT TRANSACTION");
	}

	public void rollback() {
		execQuery("ROLLBACK TRANSACTION");
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
	public void execQuery(String query) {
		connection.execQuery(String.format("'%s' query", id, query));
	}

}
