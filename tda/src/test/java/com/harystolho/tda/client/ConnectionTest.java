package com.harystolho.tda.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.harystolho.tda.client.di.Injector;
import com.harystolho.tda.shared.exception.UnrecognizedQueryException;

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

	@Test
	public void invalidQuery_ShouldThrowException() {
		assertThrows(UnrecognizedQueryException.class, () -> conn.execQuery("298bjwe91 j1(*@!&(*%"));
	}

	
	
}
