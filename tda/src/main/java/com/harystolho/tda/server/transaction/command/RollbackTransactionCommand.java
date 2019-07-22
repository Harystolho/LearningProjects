package com.harystolho.tda.server.transaction.command;

import com.harystolho.tda.server.transaction.TransactionJournal;
import com.harystolho.tda.shared.QueryResult;

public class RollbackTransactionCommand extends TransactionCommand {

	private final long transactionId;

	public RollbackTransactionCommand(long transactionId) {
		this.transactionId = transactionId;
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
