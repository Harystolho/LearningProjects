package com.harystolho.tdb_server;

import com.harystolho.tdb_server.processor.ServerDatabase;

public class Main {

	public static void main(String[] args) {
		Initializer.init();
		
		ServerDatabase db = new ServerDatabase(4455);
		db.start();
	}

}
