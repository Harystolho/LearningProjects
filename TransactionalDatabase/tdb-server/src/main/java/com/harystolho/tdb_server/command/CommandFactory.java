package com.harystolho.tdb_server.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.transaction.command.BeginTransactionCommand;
import com.harystolho.tdb_server.transaction.command.CommitTransactionCommand;
import com.harystolho.tdb_server.transaction.command.RollbackTransactionCommand;
import com.harystolho.tdb_shared.exception.UnrecognizedQueryException;

/**
 * Creates {@link Command} from a query. The commands are created based on the
 * query specification
 * 
 * @author Harystolho
 *
 */
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
		} else if (query.matches("^('\\d+'){0,1}\\s*(INSERT)\\s*(\\(.*\\))\\s*\\|\\s*\\w+")) {
			long txId = extractTransactionIdFromQuery(query);

			if (txId != -1)
				return new InsertItemCommand(txId, extractClusterNameFromQuery(query), extractValuesFromQuery(query));
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

	private String extractClusterNameFromQuery(String query) {
		try {
			String[] split = query.split("\\|");

			// Because the query may contain multiple '|', the cluster name will always be
			// the string after the last one
			return split[split.length - 1].trim();
		} catch (Exception e) {
			throw new UnrecognizedQueryException("Can't extract cluster's name from query");
		}
	}

	private Map<String, String> extractValuesFromQuery(String query) {
		try {
			int begin = query.indexOf('(');
			int end = query.indexOf(')');

			String values = query.substring(begin + 1, end);

			return Arrays.asList(values.split(",")).stream().map((pair) -> pair.split("="))
					.collect(Collectors.toMap((splitPair) -> splitPair[0], (splitPair) -> splitPair[1]));
		} catch (Exception e) {
			throw new UnrecognizedQueryException("Can't extract values from query");
		}
	}

}
