package com.harystolho.it;

import org.junit.jupiter.api.BeforeAll;
import com.harystolho.tda_client.Connection;
import com.harystolho.tdb_server.processor.InMemoryDatabase;

public class ConnectionTest {

	private static Connection conn;

	@BeforeAll
	public static void beforeAll() {
		conn = new Connection(new InMemoryDatabase());
	}
}
