package com.harystolho.tda.client.processor;

import com.harystolho.tda.server.di.Injector;
import com.harystolho.tda.server.query.QueryProcessorImpl;
import com.harystolho.tda.shared.QueryProcessor;
import com.harystolho.tda.shared.QueryResult;

public class InMemoryQueryProcessor implements QueryProcessor {

	private QueryProcessorImpl queryProcessor;

	public InMemoryQueryProcessor() {
		queryProcessor = new QueryProcessorImpl(Injector.getCommandFactory(), Injector.getCommandDispatcher());
	}

	@Override
	public QueryResult execQuery(String query) {
		return queryProcessor.execQuery(query);
	}
}
