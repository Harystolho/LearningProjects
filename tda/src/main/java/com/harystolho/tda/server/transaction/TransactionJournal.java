package com.harystolho.tda.server.transaction;

import java.util.concurrent.atomic.AtomicLong;

import com.harystolho.tda.server.command.CommandDispatcher;
import com.harystolho.tda.server.command.CommandHandler;
import com.harystolho.tda.server.transaction.command.BeginTransactionCommand;
import com.harystolho.tda.server.transaction.command.CommitTransactionCommand;
import com.harystolho.tda.server.transaction.command.RollbackTransactionCommand;
import com.harystolho.tda.server.transaction.command.TransactionCommand;
import com.harystolho.tda.shared.QueryResult;

public class TransactionJournal implements CommandHandler<TransactionCommand> {

	private final AtomicLong lastTransactionId;
	private TransactionLogger transactionLogger;

	public TransactionJournal(CommandDispatcher dispatcher, TransactionLogger transactionLogger) {
		lastTransactionId = new AtomicLong();

		this.transactionLogger = transactionLogger;
		dispatcher.register(TransactionCommand.class, this);
	}

	@Override
	public QueryResult handle(TransactionCommand command) {
		return command.execute(this);
	}

	public QueryResult handle(BeginTransactionCommand command) {
		long txId = lastTransactionId.incrementAndGet();

		transactionLogger.log(new LogBlock(txId, "BEGIN_TRANSACTION", null));

		QueryResult result = new QueryResult();
		result.put("id", txId);

		return result;
	}

	public QueryResult handle(CommitTransactionCommand command) {
		return null;
	}

	public QueryResult handle(RollbackTransactionCommand command) {
		return null;
	}
}
