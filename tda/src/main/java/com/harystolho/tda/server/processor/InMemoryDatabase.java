package com.harystolho.tda.server.processor;

import com.harystolho.tda.server.Initializer;
import com.harystolho.tda.shared.QueryProcessor;
import com.harystolho.tda.shared.QueryResult;

/**
 * Provides an instance of an in-memory database
 * 
 * @author Harystolho
 *
 */
public class InMemoryDatabase implements QueryProcessor {

	private QueryProcessor processor;

	public InMemoryDatabase() {
		processor = Initializer.getQueryProcessor();
	}

	@Override
	public QueryResult execQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
