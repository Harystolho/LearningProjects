package com.harystolho.tda.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.harystolho.tda.client.di.Injector;

public class ConnectionTest {

	private Connection conn = new Connection(Injector.getQueryProcessor());

	@Test
	public void generateTransactionId_ShouldWork() {
		assertDoesNotThrow(() -> conn.beginTransaction());
	}

	@Test
	public void multipleTransactionsShouldHaveDifferentIds() {
		assertNotEquals(conn.beginTransaction(), conn.beginTransaction());
	}

	
	
}
