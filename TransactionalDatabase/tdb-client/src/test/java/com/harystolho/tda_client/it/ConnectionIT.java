package com.harystolho.tda_client.it;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.harystolho.tda_client.Connection;
import com.harystolho.tda_client.processor.ServerQueryProcessor;

public class ConnectionIT {

	private static Connection conn;

	@BeforeAll
	public static void init() throws UnknownHostException {
		conn = new Connection(ServerQueryProcessor.create(InetAddress.getLocalHost(), 4455));
	}

	@Test
	public void beginTransaction_ShouldWork() {
		assertDoesNotThrow(() -> conn.beginTransaction());
	}

}
