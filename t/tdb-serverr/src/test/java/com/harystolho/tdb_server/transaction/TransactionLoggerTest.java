package com.harystolho.tdb_server.transaction;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_server.transaction.TransactionLogger;

@ExtendWith(MockitoExtension.class)
public class TransactionLoggerTest {

	private static TransactionLogger logger;
	private static File tempFile;

	@BeforeAll
	public static void init() throws Exception {
		tempFile = new File("temp.file");

		if (!tempFile.exists())
			tempFile.createNewFile();

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile));
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tempFile));

		logger = new TransactionLogger(oos, ois);
	}

	@AfterAll
	public static void shutdown() {
		logger.shutdown();

		if (!tempFile.delete())
			throw new RuntimeException("Error deleting test log file");
	}

	@Test
	public void createLogFileFromDirectory_ShouldFail() {
		assertThrows(RuntimeException.class, () -> {
			new TransactionLogger(System.getProperty("user.dir"));
		});
	}

	@Test
	public void createLogFileFromInvalidPathShouldFail() {
		assertThrows(RuntimeException.class, () -> {
			new TransactionLogger("215^*!^*%L");
		});
	}

	@Test
	public void writeLogBlock_ShouldWork() throws Exception {
		LogBlock block = new LogBlock(1, "COMMIT_TX", 0);

		assertDoesNotThrow(() -> logger.log(block));
	}

	@Test
	public void writeAndReadBlock_ShouldWork() {
		logger.log(new LogBlock(4, "BEGIN_TX"));
		logger.log(new LogBlock(4, "INSERT_ITEM", "name=peter"));
		logger.log(new LogBlock(4, "COMMIT_TX"));

		List<LogBlock> read = logger.read(4);

		assertEquals(read.get(0).getType(), "BEGIN_TX");
		assertEquals(read.get(1).getType(), "INSERT_ITEM");
		assertEquals(read.get(2).getType(), "COMMIT_TX");
	}

}
