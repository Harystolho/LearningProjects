package com.harystolho.tdb;

public class DatabaseLoader {

	public static Database load(String path) {
		System.out.println("Loading database from path: " + path);
		return Database.create(path);
	}

}
