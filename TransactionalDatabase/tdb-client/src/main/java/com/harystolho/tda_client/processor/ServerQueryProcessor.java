package com.harystolho.tda_client.processor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.harystolho.tdb_shared.QueryProcessor;
import com.harystolho.tdb_shared.QueryResult;

/**
 * Sends the queries to a database server somewhere else in the network
 * 
 * @author Harystolho
 *
 */
public class ServerQueryProcessor implements QueryProcessor {

	private Socket socket;

	private ServerQueryProcessor(InetAddress address, int port) {
		createSocket(address, port);
	}

	public static ServerQueryProcessor create(InetAddress address, int port) {
		return new ServerQueryProcessor(address, port);
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
		sendQuery(query);

		Object response = readResponse();

		return (QueryResult) response;
	}

	private void sendQuery(String query) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

			oos.writeUTF(query);

			oos.flush();
		} catch (IOException e) {
			throw new RuntimeException("Error sending query to server");
		}
	}

	private Object readResponse() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Error reading QueryResult from server");
		}
	}

}
