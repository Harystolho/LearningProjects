package com.harystolho.tdb_server.transaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.harystolho.tdb_server.cluster.command.DeleteItemCommand;
import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.cluster.command.UpdateItemCommand;
import com.harystolho.tdb_server.command.CommandDispatcher;
import com.harystolho.tdb_server.command.CommandHandler;
import com.harystolho.tdb_server.transaction.command.BeginTransactionCommand;
import com.harystolho.tdb_server.transaction.command.CommitTransactionCommand;
import com.harystolho.tdb_server.transaction.command.RollbackTransactionCommand;
import com.harystolho.tdb_server.transaction.command.TransactionCommand;
import com.harystolho.tdb_shared.QueryResult;
import com.harystolho.tdb_shared.exception.RollbackTransactionException;

public class TransactionJournal implements CommandHandler<TransactionCommand> {

	private final AtomicLong lastTransactionId;
	private TransactionLogger transactionLogger;
	private CommandDispatcher dispatcher;

	public TransactionJournal(CommandDispatcher dispatcher, TransactionLogger transactionLogger) {
		lastTransactionId = new AtomicLong();

		this.transactionLogger = transactionLogger;
		this.dispatcher = dispatcher;

		dispatcher.register(TransactionCommand.class, this);
	}

	@Override
	public QueryResult handle(TransactionCommand command) {
		return command.execute(this);
	}

	public QueryResult handle(BeginTransactionCommand command) {
		long txId = lastTransactionId.incrementAndGet();

		transactionLogger.log(command.toLogBlock(txId));

		QueryResult result = new QueryResult();
		result.put("id", txId);

		return result;
	}

	public QueryResult handle(CommitTransactionCommand command) {
		transactionLogger.log(command.toLogBlock());

		return QueryResult.EMPTY;
	}

	/**
	 * Rollbacks a transaction. The transaction must have been started using a
	 * {@link BeginTransactionCommand} and can't have been committed( using a
	 * {@link CommitTransactionCommand})
	 * 
	 * @param command
	 * @return
	 */
	public QueryResult handle(RollbackTransactionCommand command) {
		List<LogBlock> txs = transactionLogger.read(command.getTransactionId());

		// Transaction has to contain at least BEGIN_TX and something else to rollback
		if (txs.size() <= 1)
			return new QueryResult(new RollbackTransactionException(
					"Can't rollback transaction because it doesn't exist or hasn't begun"));

		// First log block has to be BEGIN_TX and last one can't be COMMIT_TX
		if (!txs.get(0).getType().equals("BEGIN_TX") || txs.get(txs.size() - 1).getType().equals("COMMIT_TX"))
			return new QueryResult(new RollbackTransactionException(
					"Can't rollback transaction that hasn't been started or has already been commited"));

		// Skip BEGIN_TX
		for (int i = 1; i < txs.size(); i++) {
			LogBlock block = txs.get(i);

			switch (block.getType()) {
			case "INSERT_ITEM":
				dispatcher.dispatch(InsertItemCommand.undo(block));
				break;
			case "UPDATE_ITEM":
				UpdateItemCommand.undo(block).forEach(dispatcher::dispatch);
				break;
			case "DELETE_ITEM":
				DeleteItemCommand.undo(block).forEach(dispatcher::dispatch);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + txs.get(i));
			}
		}

		return QueryResult.EMPTY;
	}
}
