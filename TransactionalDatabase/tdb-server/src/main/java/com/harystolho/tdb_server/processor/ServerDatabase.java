package com.harystolho.tdb_server.processor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.harystolho.tdb_server.Initializer;
import com.harystolho.tdb_shared.QueryProcessor;
import com.harystolho.tdb_shared.QueryResult;

/**
 * Runs a database server that can be used by clients to execute commands in the database 
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

		while (true) {
			Socket conn = null;

			try {
				conn = socket.accept();

				String query = readContent(conn);

				QueryResult result = processor.execQuery(query);

				writeResponse(conn, result);
			} catch (StreamReadException e) {
				logger.error("Error reading query from client", e);
			} catch (StreamWriteException e) {
				logger.error("Error sending query to client", e);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (IOException e) {
						logger.error("Error closing connection", e);
					}
			}
		}
	}

	private String readContent(Socket conn) throws StreamReadException {
		try {
			ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());

			return ois.readUTF();
		} catch (IOException e) {
			throw new StreamReadException(e);
		}
	}

	private void writeResponse(Socket conn, QueryResult result) throws StreamWriteException {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(conn.getOutputStream());

			oos.writeObject(result);

			oos.flush();
		} catch (IOException e) {
			throw new StreamWriteException(e);
		}
	}

	@SuppressWarnings("serial")
	private static class StreamReadException extends Exception {
		public StreamReadException(Throwable t) {
			super(t);
		}
	}

	@SuppressWarnings("serial")
	private static class StreamWriteException extends Exception {
		public StreamWriteException(Throwable t) {
			super(t);
		}
	}
}
