package com.harystolho.tda.server.di;

import com.harystolho.tda.client.processor.InMemoryQueryProcessor;
import com.harystolho.tda.server.command.CommandDispatcher;
import com.harystolho.tda.server.query.CommandFactory;
import com.harystolho.tda.server.transaction.TransactionJournal;
import com.harystolho.tda.server.transaction.command.BeginTransactionCommand;
import com.harystolho.tda.server.transaction.command.TransactionCommand;
import com.harystolho.tda.shared.QueryProcessor;

public class Injector {

	private static QueryProcessor queryProcessor;
	private static CommandFactory commandFactory;
	private static CommandDispatcher commandDispatcher;
	private static TransactionJournal transactionJournal;

	static {
		commandFactory = new CommandFactory();
		commandDispatcher = new CommandDispatcher();
		transactionJournal = new TransactionJournal();

		queryProcessor = new InMemoryQueryProcessor();
	}

	public static QueryProcessor getInMemoryQueryProcessor() {
		return queryProcessor;
	}

	public static CommandFactory getCommandFactory() {
		return commandFactory;
	}

	public static CommandDispatcher getCommandDispatcher() {
		return commandDispatcher;
	}

	public static TransactionJournal getTransactionJournal() {
		return transactionJournal;
	}

}
