package com.harystolho.tda.client.processor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.management.RuntimeErrorException;

import com.harystolho.tda.shared.QueryProcessor;
import com.harystolho.tda.shared.QueryResult;

public class DatabaseQueryProcessor implements QueryProcessor {

	private Socket socket;

	private DatabaseQueryProcessor(InetAddress address, int port) {
		createSocket(address, port);
	}

	public static DatabaseQueryProcessor create(InetAddress address, int port) {
		return new DatabaseQueryProcessor(address, port);
	}

	private void createSocket(InetAddress address, int port) {
		try {
			socket = new Socket(address, port);
		} catch (IOException e) {
			throw new RuntimeException("Error establishing connection to server");
		}
	}

	@Override
	public QueryResult execQuery(String query) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			oos.writeUTF(query);
		} catch (IOException e) {
			throw new RuntimeException("Error sending query to server");
		}

		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			Object result = ois.readObject();

			return (QueryResult) result;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Error reading QueryResult from server");
		}
	}

}