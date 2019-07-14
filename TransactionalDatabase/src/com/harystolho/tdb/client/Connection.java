package com.harystolho.tdb.client;

import com.harystolho.tdb.Database;
import com.harystolho.tdb.transaction.Transaction;

public abstract class Connection {

	private Database database;

	public Connection(Database db) {
		this.database = db;
	}

	public Transaction startTransaction() {
		return database.startTransaction();
	}

}
