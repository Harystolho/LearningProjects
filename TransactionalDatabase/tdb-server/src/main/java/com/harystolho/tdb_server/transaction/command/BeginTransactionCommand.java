package com.harystolho.tdb_server.transaction.command;

import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_server.transaction.TransactionJournal;
import com.harystolho.tdb_shared.QueryResult;

public class BeginTransactionCommand extends TransactionCommand {

	@Override
	public QueryResult execute(TransactionJournal journal) {
		return journal.handle(this);
	}

	public LogBlock toLogBlock(long txId) {
		return new LogBlock(txId, "BEGIN_TX");
	}

}
