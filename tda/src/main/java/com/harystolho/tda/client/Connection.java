package com.harystolho.tda.client;

import com.harystolho.tda.shared.QueryProcessor;
import com.harystolho.tda.shared.QueryResult;

public class Connection {

	private QueryProcessor queryProcessor;

	public Transaction beginTransaction() {
		return new Transaction(0, this);
	}

	public QueryResult execQuery(String query) {
		return null;
	}
}
