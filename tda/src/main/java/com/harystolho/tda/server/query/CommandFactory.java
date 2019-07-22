package com.harystolho.tda.server.query;

import com.harystolho.tda.server.command.Command;
import com.harystolho.tda.server.command.exception.UnrecognizedQueryException;
import com.harystolho.tda.server.transaction.command.BeginTransactionCommand;

public class CommandFactory {

	public Command<?> fromQuery(String query) {
		return new BeginTransactionCommand();
		//throw new UnrecognizedQueryException();
	}

}
