package com.harystolho.tdb_server.command;

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
import com.harystolho.tdb_server.cluster.command.DeleteItemCommand;
import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.cluster.command.ReadItemCommand;
import com.harystolho.tdb_server.cluster.command.TransactionalClusterCommand;
import com.harystolho.tdb_server.cluster.command.UpdateItemCommand;
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
	public void createDeleteItemCommand_WithOneField_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("'751246' DELETE (score=7) | GAMES");

		if (!(command instanceof DeleteItemCommand))
			fail("Command is not instance of DeleteItemCommand");

		DeleteItemCommand dic = (DeleteItemCommand) command;

		assertEquals("GAMES", dic.getClusterName());

		assertTrue(dic.getQuery().isSatisfiedBy(Item.fromMap(Map.of("score", "7"))));
		assertFalse(dic.getQuery().isSatisfiedBy(Item.fromMap(Map.of("score", "9"))));
	}

	@Test
	public void createDeleteItemCommand_WithCompositeField_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("DELETE (score=7,year=2017) | GAMES");

		if (!(command instanceof DeleteItemCommand))
			fail("Command is not instance of DeleteItemCommand");

		DeleteItemCommand dic = (DeleteItemCommand) command;

		assertEquals("GAMES", dic.getClusterName());

		assertTrue(dic.getQuery().isSatisfiedBy(Item.fromMap(Map.of("score", "7", "year", "2017"))));
		assertFalse(dic.getQuery().isSatisfiedBy(Item.fromMap(Map.of("score", "9"))));
	}

	@Test
	public void createUpdateItemCommand_ShouldWork() {
		Command<?> command = commandFactory.fromQuery("'978' UPDATE (year=2016) (score=6) | CHAMPIONSHIP");

		if (!(command instanceof UpdateItemCommand))
			fail("Command is not instance of DeleteItemCommand");

		UpdateItemCommand uic = (UpdateItemCommand) command;

		assertEquals("CHAMPIONSHIP", uic.getClusterName());

		assertEquals(978, uic.getTransactionId());
		assertEquals("6", uic.getNewValues().get("score"));
		assertTrue(uic.getQuery().isSatisfiedBy(Item.fromMap(Map.of("year", "2016"))));
	}

	@Test
	public void createUpdateItemCommand_WithoutNewValues_ShouldFail() {
		assertThrows(UnrecognizedQueryException.class, () -> {
			commandFactory.fromQuery("UPDATE (score=6) | CHAMPIONSHIP");
		});
	}

	@Test
	public void createUpdateItemCommand_WithInvalidNewValues_ShouldFail() {
		assertThrows(UnrecognizedQueryException.class, () -> {
			commandFactory.fromQuery("UPDATE (last_name=johnson) (rank,6) | CHAMPIONSHIP");
		});
	}

	@ParameterizedTest
	@ValueSource(strings = { "READ (color=black)", "DELETE (age=4)", "'77' INSERT (age=12) |" })
	public void createItemCommand_WithoutCluster_ShouldFail(String query) {
		assertThrows(UnrecognizedQueryException.class, () -> {
			commandFactory.fromQuery(query);
		});
	}

}
