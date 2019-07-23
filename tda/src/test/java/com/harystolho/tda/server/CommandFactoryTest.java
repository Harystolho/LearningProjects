package com.harystolho.tda.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.harystolho.tda.server.command.Command;
import com.harystolho.tda.server.command.CommandFactory;
import com.harystolho.tda.server.command.exception.UnrecognizedQueryException;
import com.harystolho.tda.server.transaction.command.BeginTransactionCommand;
import com.harystolho.tda.server.transaction.command.CommitTransactionCommand;
import com.harystolho.tda.server.transaction.command.RollbackTransactionCommand;

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
	@ValueSource(longs = { 0, 1, 647, 1257})
	public void rollbackCommandQuery_ShouldWork(long txId) {
		Command<?> command = commandFactory.fromQuery(String.format("'%s' ROLLBACK", txId));

		if (command instanceof RollbackTransactionCommand) {
			RollbackTransactionCommand ctc = (RollbackTransactionCommand) command;

			assertEquals(txId, ctc.getTransactionId());
		} else {
			fail("Command is not instance of RollbackTransactionCommand");
		}
	}
	
}
