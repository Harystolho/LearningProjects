package com.harystolho.tda.client;

import com.harystolho.tda.shared.QueryProcessor;
import com.harystolho.tda.shared.QueryResult;

public class Connection {

	private QueryProcessor queryProcessor;

	public Connection(QueryProcessor queryProcessor) {
		this.queryProcessor = queryProcessor;
	}

	public Transaction beginTransaction() {
		QueryResult result = execQuery("BEGIN TRANSACTION");
		
		long txId = result.getLong("id");
		
		if(txId == -1)
			throw new RuntimeException("Database didn't return transaction id");
		
		return new Transaction(txId, this);
	}

	public QueryResult execQuery(String query) {
		return queryProcessor.execQuery(query);
	}
}
