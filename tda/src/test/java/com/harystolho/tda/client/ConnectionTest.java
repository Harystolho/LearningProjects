package com.harystolho.tda.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tda.server.Initializer;
import com.harystolho.tda.shared.QueryProcessor;
import com.harystolho.tda.shared.QueryResult;
import com.harystolho.tda.shared.exception.UnrecognizedQueryException;

@ExtendWith(MockitoExtension.class)
public class ConnectionTest {

	@InjectMocks
	private Connection conn;

	@Mock
	private QueryProcessor processor;

	@Test
	public void generateTransactionId_ShouldWork() {
		Mockito.when(processor.execQuery(Mockito.same("BEGIN TRANSACTION"))).thenAnswer((inv) -> {
			QueryResult qr = new QueryResult();
			qr.put("id", new Random().nextLong());
			return qr;
		});

		assertDoesNotThrow(() -> conn.beginTransaction());
	}

	@Test
	public void invalidQuery_ShouldThrowException() {
		Mockito.when(processor.execQuery(Mockito.any()))
				.thenAnswer((inv) -> new QueryResult(new UnrecognizedQueryException()));

		assertThrows(UnrecognizedQueryException.class, () -> conn.execQuery("298bjwe91 j1(*@!&(*%"));
	}

}
