package com.harystolho.tda.server.transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Writes/Reads transaction from disk
 * 
 * @author Harystolho
 *
 */
public class TransactionLogger implements CommandLogger {

	private AtomicLong logSequenceNumber;

	private ObjectOutputStream oos;

	public TransactionLogger(String logFilePath) {
		logSequenceNumber = new AtomicLong(0);

		createOutputStream(logFilePath);
	}

	@Override
	public void log(LogBlock logBlock) {
		try {
			oos.writeLong(logSequenceNumber.getAndIncrement());
			oos.writeLong(logBlock.getTransactionId());
			oos.writeUTF(logBlock.getType());
			oos.writeObject(logBlock.getObject());

			oos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected Collection<LogBlock> readAll() {
		return Collections.emptyList();
	}

	/**
	 * Read all {@link LogBlock} written using the specified transaction id.
	 * 
	 * @param transactionId
	 * @return the log blocks in the order they were logged
	 */
	protected Collection<LogBlock> read(long transactionId) {
		return Collections.emptyList();
	}

	private void createOutputStream(String logFilePath) {
		try {
			oos = new ObjectOutputStream(new FileOutputStream(getFileFromPath(logFilePath), true));
		} catch (IOException e) {
			throw new RuntimeException("Can't create output stream to log transactions");
		}
	}

	private File getFileFromPath(String path) throws IOException {
		File file = new File(path);

		if (file.isDirectory())
			throw new RuntimeException("The log path must be a file, not a directory [Path=" + path + "]");

		if (!file.exists())
			file.createNewFile();

		return file;
	}

}
