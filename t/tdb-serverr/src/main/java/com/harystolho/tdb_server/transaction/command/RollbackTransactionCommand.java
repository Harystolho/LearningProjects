package com.harystolho.tdb_server.transaction.command;

import com.harystolho.tdb_server.transaction.TransactionJournal;
import com.harystolho.tdb_shared.QueryResult;

public class RollbackTransactionCommand extends TransactionCommand {

	private final long transactionId;

	public RollbackTransactionCommand(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	@Override
	public QueryResult execute(TransactionJournal journal) {
		return journal.handle(this);
	}

	@Override
	public QueryResult undo(TransactionJournal journal) {

		return null;
	}

}