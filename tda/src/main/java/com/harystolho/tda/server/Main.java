package com.harystolho.tda.server;

import com.harystolho.tda.server.processor.ServerDatabase;

public class Main {

	public static void main(String[] args) {
		Initializer.init();
		
		ServerDatabase db = new ServerDatabase(4455);
		db.start();
	}

}
