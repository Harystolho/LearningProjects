package com.harystolho.tda.server;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.harystolho.tda.server.transaction.TransactionLogger;

public class TransactionLoggerTest {

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
	
}
