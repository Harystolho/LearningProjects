package com.harystolho.tdb_server.transaction.command;

import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_server.transaction.TransactionJournal;
import com.harystolho.tdb_shared.QueryResult;

public class BeginTransactionCommand extends TransactionCommand {

	@Override
	public QueryResult execute(TransactionJournal journal) {
		return journal.handle(this);
	}

	@Override
	public QueryResult undo(TransactionJournal journal) {

		return null;
	}

}
