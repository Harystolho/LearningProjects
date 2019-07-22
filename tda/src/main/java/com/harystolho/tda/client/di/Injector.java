package com.harystolho.tda.client.di;

import com.harystolho.tda.server.command.CommandDispatcher;
import com.harystolho.tda.server.query.CommandFactory;
import com.harystolho.tda.server.query.QueryProcessorImpl;
import com.harystolho.tda.server.transaction.TransactionJournal;
import com.harystolho.tda.shared.QueryProcessor;

/**
 * Initializes and configures components
 * 
 * @author Harystolho
 *
 */
public class Injector {

	private static QueryProcessor queryProcessor;
	private static CommandFactory commandFactory;
	private static CommandDispatcher commandDispatcher;
	private static TransactionJournal transactionJournal;

	static {
		commandFactory = new CommandFactory();
		commandDispatcher = new CommandDispatcher();
		transactionJournal = new TransactionJournal(commandDispatcher);

		queryProcessor = new QueryProcessorImpl(commandFactory, commandDispatcher);
	}

	public static QueryProcessor getQueryProcessor() {
		return queryProcessor;
	}

}
