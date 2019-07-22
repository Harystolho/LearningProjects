package com.harystolho.tda.client;

public class Transaction {

	private long id;
	private Connection connection;

	public Transaction(long id, Connection connection) {
		this.id = id;
		this.connection = connection;
	}

	public void commit() {
		
	}

	public void rollback() {

	}

	public void execQuery(String query) {
		connection.execQuery(query);
	}

}
