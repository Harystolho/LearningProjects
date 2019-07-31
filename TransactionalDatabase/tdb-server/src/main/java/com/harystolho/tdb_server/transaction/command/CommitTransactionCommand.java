package com.harystolho.tdb_server.transaction.command;

import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_server.transaction.TransactionJournal;
import com.harystolho.tdb_shared.QueryResult;

public class CommitTransactionCommand extends TransactionCommand {

	private final long transactionId;

	public CommitTransactionCommand(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public LogBlock toLogBlock() {
		return new LogBlock(transactionId, "COMMIT_TX");
	}

	@Override
	public QueryResult execute(TransactionJournal journal) {
		return journal.handle(this);
	}

}
