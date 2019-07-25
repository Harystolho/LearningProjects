package com.harystolho.tda.server.transaction.command;

import com.harystolho.tda.server.command.Command;
import com.harystolho.tda.server.transaction.TransactionJournal;

public abstract class TransactionCommand implements Command<TransactionJournal> {

	@Override
	public Class<?> getHandlerClassType() {
		return TransactionCommand.class;
	}

}
