package com.harystolho.tda.client.di;

import com.harystolho.tda.server.command.CommandDispatcher;
import com.harystolho.tda.server.command.CommandFactory;
import com.harystolho.tda.server.config.DatabaseProperties;
import com.harystolho.tda.server.query.QueryProcessorImpl;
import com.harystolho.tda.server.transaction.TransactionJournal;
import com.harystolho.tda.server.transaction.TransactionLogger;
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
	private static TransactionLogger transactionLogger;
	private static DatabaseProperties databaseProperties;

	static {
		commandFactory = new CommandFactory();
		commandDispatcher = new CommandDispatcher();
		databaseProperties = new DatabaseProperties();
		transactionLogger = new TransactionLogger(databaseProperties.get("log_file"));
		transactionJournal = new TransactionJournal(commandDispatcher, transactionLogger);

		queryProcessor = new QueryProcessorImpl(commandFactory, commandDispatcher);
	}

	public static QueryProcessor getQueryProcessor() {
		return queryProcessor;
	}

}
