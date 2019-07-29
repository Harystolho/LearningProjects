package com.harystolho.tdb_server;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.harystolho.tdb_server.cluster.ClusterCatalog;
import com.harystolho.tdb_server.command.CommandDispatcher;
import com.harystolho.tdb_server.command.CommandFactory;
import com.harystolho.tdb_server.config.DatabaseProperties;
import com.harystolho.tdb_server.query.QueryProcessorImpl;
import com.harystolho.tdb_server.transaction.TransactionJournal;
import com.harystolho.tdb_server.transaction.TransactionLogger;
import com.harystolho.tdb_shared.QueryProcessor;

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
	private static ClusterCatalog clusterCatalog;
	private static DatabaseProperties databaseProperties;

	public static void init() {
		BasicConfigurator.configure(); // Initialize logger

		logger.info("Initializing database components");

		commandFactory = new CommandFactory();
		commandDispatcher = new CommandDispatcher();
		databaseProperties = new DatabaseProperties();
		transactionLogger = new TransactionLogger(databaseProperties.get("log_file"));
		transactionJournal = new TransactionJournal(commandDispatcher, transactionLogger);
		clusterCatalog = new ClusterCatalog(commandDispatcher, transactionLogger);

		queryProcessor = new QueryProcessorImpl(commandFactory, commandDispatcher);

		logger.info("Database components initialization complete");
	}

	public static void shutdown() {
		logger.info("Shutting application down");
	}

	public static QueryProcessor getQueryProcessor() {
		return queryProcessor;
	}

}
