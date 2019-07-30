package com.harystolho.tdb_server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.harystolho.tdb_server.cluster.Item;
import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.cluster.command.ReadItemCommand;
import com.harystolho.tdb_server.cluster.command.TransactionalClusterCommand;
import com.harystolho.tdb_server.command.Command;
import com.harystolho.tdb_server.command.CommandFactory;
import com.harystolho.tdb_server.transaction.command.BeginTransactionCommand;
import com.harystolho.tdb_server.transaction.command.CommitTransactionCommand;
import com.harystolho.tdb_server.transaction.command.RollbackTransactionCommand;
import com.harystolho.tdb_shared.exception.UnrecognizedQueryException;

public class CommandFactoryTest {

	private CommandFactory commandFactory;

	public CommandFactoryTest() {
		commandFactory = new CommandFactory();
	}

	@Test
	public void emptyQuery_ShouldThrowError() {
		assertThrows(UnrecognizedQueryException.class, () -> commandFactory.fromQuery(""));
	}

	@Test
	public void invalidQuery_ShouldThrowError() {
		assertThrows(UnrecognizedQueryException.class, () -> commandFactory.fromQuery("so18guwk609 fg82"));
	}

	@Test
	public void beginTransactionQuery_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("BEGIN TRANSACTION");

		assertEquals(BeginTransactionCommand.class, command.getClass());
	}

	@ParameterizedTest
	@ValueSource(longs = { 0, 1, 457, 1050 })
	public void commitTransactionQuery_ShouldWork(long txId) {
		Command<?> command = commandFactory.fromQuery(String.format("'%s' COMMIT", txId));

		if (command instanceof CommitTransactionCommand) {
			CommitTransactionCommand ctc = (CommitTransactionCommand) command;

			assertEquals(txId, ctc.getTransactionId());
		} else {
			fail("Command is not instance of CommitTransactionCommand");
		}
	}

	@ParameterizedTest
	@ValueSource(longs = { 0, 1, 647, 1257 })
	public void rollbackCommandQuery_ShouldWork(long txId) {
		Command<?> command = commandFactory.fromQuery(String.format("'%s' ROLLBACK", txId));

		if (!(command instanceof RollbackTransactionCommand))
			fail("Command is not instance of RollbackTransactionCommand");

		RollbackTransactionCommand ctc = (RollbackTransactionCommand) command;

		assertEquals(txId, ctc.getTransactionId());
	}

	@Test
	public void createInsertItemCommand_WithTransactionId_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("'12' INSERT (name=JD,year=2014) | TRACTORS");

		if (!(command instanceof InsertItemCommand))
			fail("Command is not instance of InsertItemCommand");

		InsertItemCommand iic = (InsertItemCommand) command;

		assertEquals(12, iic.getTransactionId());
		assertEquals("TRACTORS", iic.getClusterName());

		Map<String, String> values = iic.getValues();
		assertEquals("JD", values.get("name"));
		assertEquals("2014", values.get("year"));
	}

	@Test
	public void createInsertItemCommand_WithoutTransactionId_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("INSERT (country=BR,membership=GOLD) | MEMBERS");

		if (!(command instanceof InsertItemCommand))
			fail("Command is not instance of InsertItemCommand");

		InsertItemCommand iic = (InsertItemCommand) command;

		assertEquals(TransactionalClusterCommand.NO_TRANSACTION, iic.getTransactionId());
		assertEquals("MEMBERS", iic.getClusterName());

		Map<String, String> values = iic.getValues();
		assertEquals("BR", values.get("country"));
		assertEquals("GOLD", values.get("membership"));
	}

	@Test
	public void createInsertItemCommandFromQueryWithoutSpaces_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("'55'INSERT(color=yellow)|FLAGS");

		if (!(command instanceof InsertItemCommand))
			fail("Command is not instance of InsertItemCommand");

		InsertItemCommand iic = (InsertItemCommand) command;

		assertEquals(55, iic.getTransactionId());
		assertEquals("FLAGS", iic.getClusterName());

		Map<String, String> values = iic.getValues();
		assertEquals("yellow", values.get("color"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "ag e", "name=", "country,zip_code", "", "12_12" })
	public void createInsertItemCommandWithInvalidValues_ShouldFail(String values) {
		assertThrows(UnrecognizedQueryException.class, () -> {
			commandFactory.fromQuery(String.format("'55'INSERT(%s)|FLAGS", values));
		});
	}

	@Test
	public void createInsertItemCommandWithInvalidClusterName_ShouldFail() {
		assertThrows(UnrecognizedQueryException.class, () -> {
			commandFactory.fromQuery("'77' INSERT (age=12) |");
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "'' COMMIT", "'' INSERT (name=joe) | CITZENS", "' ' INSERT (name=joe) | CITZENS" })
	public void createCommandWithInvalidTransactionId_ShouldFail(String values) {
		assertThrows(UnrecognizedQueryException.class, () -> {
			commandFactory.fromQuery(values);
		});
	}

	@Test
	public void createReadItemCommand_WithOneField_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("READ (year=2008) | SONGS");

		if (!(command instanceof ReadItemCommand))
			fail("Command is not instance of ReadItemCommand");

		ReadItemCommand ric = (ReadItemCommand) command;

		assertEquals("SONGS", ric.getClusterName());

		assertTrue(ric.getQuery().isSatisfiedBy(Item.fromMap(Map.of("year", "2008"))));
		assertFalse(ric.getQuery().isSatisfiedBy(Item.fromMap(Map.of("year", "2018"))));
	}

	@Test
	public void createReadItemCommand_WithCompositeField_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("READ (age=24,country=BR) | USERS");

		if (!(command instanceof ReadItemCommand))
			fail("Command is not instance of ReadItemCommand");

		ReadItemCommand ric = (ReadItemCommand) command;

		assertEquals("USERS", ric.getClusterName());

		assertTrue(ric.getQuery().isSatisfiedBy(Item.fromMap(Map.of("age", "24", "country", "BR"))));
		assertFalse(ric.getQuery().isSatisfiedBy(Item.fromMap(Map.of("age", "24"))));
	}

	@Test
	public void createReadItemCommand_WithoutCluster_ShouldFail() {
		assertThrows(UnrecognizedQueryException.class, () -> {
			commandFactory.fromQuery("READ (color=black)");
		});
	}
}
