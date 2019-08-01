package com.harystolho.tdb_server.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.command.CommandDispatcher;
import com.harystolho.tdb_server.transaction.command.BeginTransactionCommand;
import com.harystolho.tdb_server.transaction.command.CommitTransactionCommand;
import com.harystolho.tdb_server.transaction.command.RollbackTransactionCommand;
import com.harystolho.tdb_shared.QueryResult;
import com.harystolho.tdb_shared.exception.RollbackTransactionException;

@ExtendWith(MockitoExtension.class)
public class TransactionJournalTest {

	@InjectMocks
	TransactionJournal journal;

	@Mock
	private CommandDispatcher commandDispatcher;
	@Mock
	private TransactionLogger transactionLogger;

	@Test
	public void transactionShouldHaveUniqueIds() {
		QueryResult result1 = journal.handle(new BeginTransactionCommand());
		QueryResult result2 = journal.handle(new BeginTransactionCommand());

		assertNotEquals(result1.getLong("id"), result2.getLong("id"));
	}

	@Test
	public void rollbackTransaction_ShouldWork() {
		BeginTransactionCommand btc = new BeginTransactionCommand();
		InsertItemCommand iic1 = new InsertItemCommand(145, "INV", null);
		InsertItemCommand iic2 = new InsertItemCommand(145, "INV", null);

		Mockito.when(transactionLogger.read(Mockito.anyLong()))
				.thenReturn(Arrays.asList(btc.toLogBlock(145), iic1.toLogBlock(12), iic2.toLogBlock(13)));

		journal.handle(new RollbackTransactionCommand(145));

		Mockito.verify(commandDispatcher, Mockito.times(2)).dispatch(Mockito.any());
	}

	@Test
	public void rollbackTransaction_WithoutBeginTransactionCommand_ShouldFail() {
		InsertItemCommand iic1 = new InsertItemCommand(145, "INV", null);
		InsertItemCommand iic2 = new InsertItemCommand(145, "INV", null);

		Mockito.when(transactionLogger.read(Mockito.anyLong()))
				.thenReturn(Arrays.asList(iic1.toLogBlock(12), iic2.toLogBlock(13)));

		QueryResult result = journal.handle(new RollbackTransactionCommand(145));

		assertEquals(RollbackTransactionException.class, result.getException().getClass());
	}

	@Test
	public void rollbackTransaction_ThatHasBeenCommited_ShouldFail() {
		BeginTransactionCommand btc = new BeginTransactionCommand();
		InsertItemCommand iic1 = new InsertItemCommand(145, "INV", null);
		CommitTransactionCommand ctc = new CommitTransactionCommand(145);

		Mockito.when(transactionLogger.read(Mockito.anyLong()))
				.thenReturn(Arrays.asList(btc.toLogBlock(145), iic1.toLogBlock(12), ctc.toLogBlock()));

		QueryResult result = journal.handle(new RollbackTransactionCommand(145));

		assertEquals(RollbackTransactionException.class, result.getException().getClass());
	}

	@Test
	public void rollbackTransaction_ThatDoesNotExist_ShouldFail() {
		Mockito.when(transactionLogger.read(Mockito.anyLong())).thenReturn(Collections.emptyList());

		QueryResult result = journal.handle(new RollbackTransactionCommand(145));

		assertEquals(RollbackTransactionException.class, result.getException().getClass());
	}

}
