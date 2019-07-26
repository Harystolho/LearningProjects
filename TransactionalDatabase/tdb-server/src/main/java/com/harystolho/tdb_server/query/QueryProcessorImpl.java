package com.harystolho.tdb_server.query;

import com.harystolho.tdb_server.command.Command;
import com.harystolho.tdb_server.command.CommandDispatcher;
import com.harystolho.tdb_server.command.CommandFactory;
import com.harystolho.tdb_shared.QueryProcessor;
import com.harystolho.tdb_shared.QueryResult;
import com.harystolho.tdb_shared.exception.UnrecognizedQueryException;

public class QueryProcessorImpl implements QueryProcessor {

	private CommandFactory commandFactory;
	private CommandDispatcher commandDispatcher;

	public QueryProcessorImpl(CommandFactory commandFactory, CommandDispatcher commandDispatcher) {
		this.commandFactory = commandFactory;
		this.commandDispatcher = commandDispatcher;
	}

	@Override
	public QueryResult execQuery(String query) {
		Command<?> command = null;

		try {
			command = commandFactory.fromQuery(query);
		} catch (UnrecognizedQueryException e) {
			return new QueryResult(e);
		}

		return commandDispatcher.dispatch(command);
	}

}
