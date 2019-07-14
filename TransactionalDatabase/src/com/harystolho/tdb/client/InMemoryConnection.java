package com.harystolho.tdb.client;

import com.harystolho.tdb.Transaction;

public class InMemoryConnection implements Connection {

	public static InMemoryConnection connect(String dbName) {
		return new InMemoryConnection();
	}

	@Override
	public Transaction startTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

}
