package com.harystolho.tdb.client;

import com.harystolho.tdb.Database;
import com.harystolho.tdb.DatabaseLoader;

public class InMemoryConnection extends Connection {

	public InMemoryConnection(Database db) {
		super(db);
	}

	public static InMemoryConnection connect(String dbName) {
		return new InMemoryConnection(DatabaseLoader.load("db/" + dbName + ".tdb"));
	}

}
