package com.harystolho.tdb_server.command;

import com.harystolho.tdb_server.transaction.command.BeginTransactionCommand;
import com.harystolho.tdb_server.transaction.command.CommitTransactionCommand;
import com.harystolho.tdb_server.transaction.command.RollbackTransactionCommand;
import com.harystolho.tdb_shared.exception.UnrecognizedQueryException;

public class CommandFactory {

	public Command<?> fromQuery(String query) {

		if (query.equalsIgnoreCase("BEGIN TRANSACTION")) {
			return new BeginTransactionCommand();
		} else if (query.matches("'\\d+'\\s*(COMMIT)$")) {
			long txId = extractTransactionIdFromQuery(query);

			if (txId != -1)
				return new CommitTransactionCommand(txId);
		} else if (query.matches("'\\d+'\\s*(ROLLBACK)$")) {
			long txId = extractTransactionIdFromQuery(query);

			if (txId != -1)
				return new RollbackTransactionCommand(txId);
		}

		throw new UnrecognizedQueryException();
	}

	private long extractTransactionIdFromQuery(String query) {
		try {
			return Long.valueOf(query.split("'")[1]);
		} catch (Exception e) {
			return -1;
		}
	}

}
