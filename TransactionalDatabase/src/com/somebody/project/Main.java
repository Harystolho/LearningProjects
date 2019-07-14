package com.somebody.project;

import com.harystolho.tdb.client.Connection;
import com.harystolho.tdb.client.InMemoryConnection;

public class Main {

	public static void main(String[] args) {
		Connection conn = InMemoryConnection.connect("work_db");

		conn.startTransaction();

	}

}
