package com.harystolho.tda.client.processor;

import com.harystolho.tda.server.query.QueryProcessorImpl;
import com.harystolho.tda.shared.QueryProcessor;
import com.harystolho.tda.shared.QueryResult;

public class InMemoryQueryProcessor implements QueryProcessor {

	private QueryProcessorImpl queryProcessor;

	@Override
	public QueryResult execQuery(String query) {
		return queryProcessor.execQuery(query);
	}
}
