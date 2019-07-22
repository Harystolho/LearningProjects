package com.harystolho.tda.server.query;

import com.harystolho.tda.server.command.Command;
import com.harystolho.tda.server.command.exception.UnrecognizedQueryException;

public class CommandFactory {

	public Command<?> fromQuery(String query) {
		throw new UnrecognizedQueryException();
	}

}
