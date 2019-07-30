package com.harystolho.it;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.harystolho.tda_client.Connection;
import com.harystolho.tdb_server.processor.InMemoryDatabase;

public class ConnectionTest {

	private static Connection conn;

	@BeforeAll
	public static void beforeAll() {
		conn = new Connection(new InMemoryDatabase());
	}
}
