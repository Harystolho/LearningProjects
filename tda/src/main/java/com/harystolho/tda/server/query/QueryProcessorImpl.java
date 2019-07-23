package com.harystolho.tda.server.query;

import com.harystolho.tda.server.command.Command;
import com.harystolho.tda.server.command.CommandDispatcher;
import com.harystolho.tda.server.command.CommandFactory;
import com.harystolho.tda.server.command.exception.UnrecognizedQueryException;
import com.harystolho.tda.shared.QueryProcessor;
import com.harystolho.tda.shared.QueryResult;

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
			// TODO
		}

		return commandDispatcher.dispatch(command);
	}

}
