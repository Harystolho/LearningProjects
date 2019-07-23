package com.harystolho.tda.server;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tda.server.command.CommandDispatcher;
import com.harystolho.tda.server.transaction.TransactionJournal;
import com.harystolho.tda.server.transaction.TransactionLogger;
import com.harystolho.tda.server.transaction.command.BeginTransactionCommand;
import com.harystolho.tda.shared.QueryResult;

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

}
