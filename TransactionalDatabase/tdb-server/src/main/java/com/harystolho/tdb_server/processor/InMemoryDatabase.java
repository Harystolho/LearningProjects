package com.harystolho.tdb_server.processor;

import com.harystolho.tdb_server.Initializer;
import com.harystolho.tdb_shared.QueryProcessor;
import com.harystolho.tdb_shared.QueryResult;

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
		return processor.execQuery(query);
	}

}
