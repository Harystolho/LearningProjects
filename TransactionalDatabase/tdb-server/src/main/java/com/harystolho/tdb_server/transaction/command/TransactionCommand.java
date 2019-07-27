package com.harystolho.tdb_server.transaction.command;

import com.harystolho.tdb_server.command.Command;
import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_server.transaction.TransactionJournal;

public abstract class TransactionCommand implements Command<TransactionJournal> {

	@Override
	public Class<?> getHandlerClassType() {
		return TransactionCommand.class;
	}

}