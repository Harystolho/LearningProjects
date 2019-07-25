package com.harystolho.tda.server.transaction.command;

import com.harystolho.tda.server.transaction.TransactionJournal;
import com.harystolho.tda.shared.QueryResult;

public class CommitTransactionCommand extends TransactionCommand {

	private final long transactionId;

	public CommitTransactionCommand(long transactionId) {
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
