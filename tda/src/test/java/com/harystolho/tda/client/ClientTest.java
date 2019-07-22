package com.harystolho.tda.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import com.harystolho.tda.server.di.Injector;

public class ClientTest {

	private Connection conn = new Connection(Injector.getInMemoryQueryProcessor());

	@Test
	public void generateTransactionId_ShouldWork() {
		assertDoesNotThrow(() -> conn.beginTransaction());
	}

}
