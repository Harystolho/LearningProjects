package com.harystolho.tdb_server.processor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.harystolho.tdb_server.Initializer;
import com.harystolho.tdb_shared.QueryProcessor;
import com.harystolho.tdb_shared.QueryResult;

/**
 * Runs a database server that can be used by clients
 * 
 * @author Harystolho
 *
 */
public class ServerDatabase {

	private static final Logger logger = Logger.getLogger(ServerDatabase.class.getName());

	private ServerSocket socket;

	private QueryProcessor processor = Initializer.getQueryProcessor();

	public ServerDatabase(int port) {
		try {
			socket = new ServerSocket(port);
			logger.info("Initialing server on port " + port);
		} catch (IOException e) {
			throw new RuntimeException("Error initialing server. Try a different port");
		}
	}

	public void start() {
		logger.info("Accepting client connections");

		try {
			while (true) {
				Socket conn = socket.accept();

				String query = readContent(conn);

				QueryResult result = processor.execQuery(query);

				writeResponse(conn, result);
			}
		} catch (Exception e) {

		}
	}

	private String readContent(Socket conn) {
		try {
			ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());

			Object object = ois.readObject();

			return (String) object;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Error reading query from client");
		}
	}

	private void writeResponse(Socket conn, QueryResult result) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(conn.getOutputStream());

			oos.writeObject(result);
		} catch (IOException e) {
			throw new RuntimeException("Error sending query to server");
		}
	}

}
