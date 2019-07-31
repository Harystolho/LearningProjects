package com.harystolho.tdb_server.transaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
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

		transactionLogger.log(new LogBlock(txId, "BEGIN_TX"));

		QueryResult result = new QueryResult();
		result.put("id", txId);

		return result;
	}

	public QueryResult handle(CommitTransactionCommand command) {
		transactionLogger.log(command.toLogBlock());

		return QueryResult.EMPTY;
	}

	public QueryResult handle(RollbackTransactionCommand command) {
		List<LogBlock> txs = transactionLogger.read(command.getTransactionId());

		// Transaction has to contain at least BEGIN_TX and COMMIT_TX log blocks to be
		// valid
		if (txs.size() <= 1)
			return new QueryResult(new RollbackTransactionException());

		// First log block has to be BEGIN_TX and last one has to be COMMIT_TX
		if (!txs.get(0).getType().equals("BEGIN_TX") || !txs.get(txs.size() - 1).getType().equals("COMMIT_TX"))
			return new QueryResult(new RollbackTransactionException());

		// txs[0] = BEGIN_TX
		// txs[size -1] = COMMIT_TX
		for (int i = 1; i < txs.size() - 2; i++) {
			LogBlock block = txs.get(i);

			switch (block.getType()) {
			case "INSERT_ITEM":
				dispatcher.dispatch(InsertItemCommand.undo(block));
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + txs.get(i));
			}
		}

		return QueryResult.EMPTY;
	}
}
