package com.harystolho.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.harystolho.tda_client.Connection;
import com.harystolho.tda_client.Transaction;
import com.harystolho.tdb_server.processor.InMemoryDatabase;
import com.harystolho.tdb_shared.QueryResult;
import com.harystolho.tdb_shared.exception.RollbackTransactionException;

public class ConnectionTest {

	private static Connection conn;

	@BeforeAll
	public static void beforeAll() {
		conn = new Connection(new InMemoryDatabase());
	}

	@Test
	public void rollbackOneCommand_ShouldWork() {
		Transaction tx = conn.beginTransaction();

		tx.execQuery("INSERT (name=peter,age=14) | USERS");

		QueryResult result = conn.execQuery("READ (age=14) | USERS");
		List<Map<String, String>> items = result.getList("items");
		assertEquals("peter", items.get(0).get("name"));

		tx.rollback();

		QueryResult resultAfterRollback = conn.execQuery("READ (age=14) | USERS");
		List<Map<String, String>> itemsAfterRollback = resultAfterRollback.getList("items");
		assertEquals(0, itemsAfterRollback.size());
	}

	@Test
	public void rollbackCommitedCommand_ShouldFail() {
		Transaction tx = conn.beginTransaction();

		tx.execQuery("INSERT (name=john,city=tly) | PEOPLE");
		tx.commit();

		assertThrows(RollbackTransactionException.class, () -> {
			tx.rollback();
		});
	}

}
