package com.harystolho.tdb.transaction;

public class Transaction {

	private long id;

	public Transaction(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	public void commit() {

	}

	public void rollback() {

	}

}
