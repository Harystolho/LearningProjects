package com.harystolho.tdb_server.command;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.harystolho.tdb_server.cluster.command.DeleteItemCommand;
import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.cluster.command.ReadItemCommand;
import com.harystolho.tdb_server.cluster.command.TransactionalClusterCommand;
import com.harystolho.tdb_server.cluster.command.UpdateItemCommand;
import com.harystolho.tdb_server.cluster.query.ItemFieldQuery;
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

		if (query.equals("BEGIN TRANSACTION")) {
			return new BeginTransactionCommand();

		} else if (query.matches("'\\d+'\\s*(COMMIT)$")) { // TODO create a virable to hold compiled regex
			return new CommitTransactionCommand(extractTransactionIdFromQuery(query));

		} else if (query.matches("'\\d+'\\s*(ROLLBACK)$")) {
			return new RollbackTransactionCommand(extractTransactionIdFromQuery(query));

		} else if (query.matches("^('\\d+'){0,1}\\s*(INSERT)\\s*(\\(.*\\))\\s*\\|\\s*\\w+")) {
			if (constainsTransactionId(query))
				return new InsertItemCommand(extractTransactionIdFromQuery(query), extractClusterNameFromQuery(query),
						extractValuesFromQuery(query));

			return new InsertItemCommand(TransactionalClusterCommand.NO_TRANSACTION, extractClusterNameFromQuery(query),
					extractValuesFromQuery(query));

		} else if (query.matches("(READ)\\s*(\\(.*\\))\\s*\\|\\s*\\w+")) {
			return new ReadItemCommand(extractClusterNameFromQuery(query),
					ItemFieldQuery.fromMap(extractValuesFromQuery(query)));

		} else if (query.matches("^('\\d+'){0,1}\\s*(DELETE)\\s*(\\(.*\\))\\s*\\|\\s*\\w+")) {
			if (constainsTransactionId(query))
				return new DeleteItemCommand(extractTransactionIdFromQuery(query), extractClusterNameFromQuery(query),
						ItemFieldQuery.fromMap(extractValuesFromQuery(query)));

			return new DeleteItemCommand(TransactionalClusterCommand.NO_TRANSACTION, extractClusterNameFromQuery(query),
					ItemFieldQuery.fromMap(extractValuesFromQuery(query)));

		} else if (query.matches("^('\\d+'){0,1}\\s*(UPDATE)\\s*(\\(.*\\))\\s*(\\(.*\\))\\s*\\|\\s*\\w+")) {
			// If the query is similar to "UPDATE (name=john) (age=12) ...", to separate the
			// query from the values to update, they have to be separated and interpreted
			// separately
			String[] values = query.split("\\)");

			Map<String, String> queryMap = extractValuesFromQuery(values[0] + ")");
			Map<String, String> newValuesMap = extractValuesFromQuery(values[1] + ")");

			if (constainsTransactionId(query))
				return new UpdateItemCommand(extractTransactionIdFromQuery(query), extractClusterNameFromQuery(query),
						ItemFieldQuery.fromMap(queryMap), newValuesMap);

			return new UpdateItemCommand(TransactionalClusterCommand.NO_TRANSACTION, extractClusterNameFromQuery(query),
					ItemFieldQuery.fromMap(queryMap), newValuesMap);

		}

		throw new UnrecognizedQueryException();
	}

	private boolean constainsTransactionId(String query) {
		return query.matches("('\\d+').*");
	}

	private long extractTransactionIdFromQuery(String query) {
		try {
			return Long.valueOf(query.split("'")[1]);
		} catch (Exception e) {
			throw new UnrecognizedQueryException("Error extracting transaction id from query");
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
			throw new UnrecognizedQueryException("Error extracting values from query");
		}
	}

}
