package com.harystolho.tda.server.config;

import java.io.IOException;
import java.util.Properties;

public class DatabaseProperties {

	private Properties properties;

	public DatabaseProperties() {
		properties = new Properties();

		loadProperties();
	}

	private void loadProperties() {
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream("db.config"));
		} catch (IOException e) {
			System.out.println("Error reading database properties");
			System.exit(-1);
		}
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

}
