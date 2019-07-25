package com.harystolho.tda.server;

import java.util.logging.Logger;

import com.harystolho.tda.server.command.CommandDispatcher;
import com.harystolho.tda.server.command.CommandFactory;
import com.harystolho.tda.server.config.DatabaseProperties;
import com.harystolho.tda.server.query.QueryProcessorImpl;
import com.harystolho.tda.server.transaction.TransactionJournal;
import com.harystolho.tda.server.transaction.TransactionLogger;
import com.harystolho.tda.shared.QueryProcessor;

/**
 * Initializes and injects dependencies into components
 * 
 * @author Harystolho
 *
 */
public class Initializer {

	private static final Logger logger = Logger.getLogger(Initializer.class.getName());

	private static QueryProcessor queryProcessor;
	private static CommandFactory commandFactory;
	private static CommandDispatcher commandDispatcher;
	private static TransactionJournal transactionJournal;
	private static TransactionLogger transactionLogger;
	private static DatabaseProperties databaseProperties;

	public static void init() {
		logger.info("Initializing database components");

		commandFactory = new CommandFactory();
		commandDispatcher = new CommandDispatcher();
		databaseProperties = new DatabaseProperties();
		transactionLogger = new TransactionLogger(databaseProperties.get("log_file"));
		transactionJournal = new TransactionJournal(commandDispatcher, transactionLogger);

		queryProcessor = new QueryProcessorImpl(commandFactory, commandDispatcher);

		logger.info("Database components initializing complete");
	}

	public static void shutdown() {
		logger.info("Shutting application down");
	}

	public static QueryProcessor getQueryProcessor() {
		return queryProcessor;
	}

}
