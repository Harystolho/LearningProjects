package com.harystolho.tdb_server.transaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.harystolho.tdb_shared.exception.DatabaseException;


/**
 * Writes/Reads transaction from disk
 * 
 * @author Harystolho
 *
 */
public class TransactionLogger implements CommandLogger {

	private AtomicLong logSequenceNumber;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private TransactionLogger() {
		logSequenceNumber = new AtomicLong(0);
	}

	public TransactionLogger(String logFilePath) {
		this();

		createStreams(logFilePath);
	}

	public TransactionLogger(ObjectOutputStream oos, ObjectInputStream ois) {
		this();
		this.oos = oos;
		this.ois = ois;
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
		List<LogBlock> blocks = new ArrayList<>();

		try {
			while (ois.available() > 0) {
				ois.readLong(); // Discard LSN
				long txId = ois.readLong();
				String type = ois.readUTF();
				Object object = ois.readObject();

				blocks.add(new LogBlock(txId, type, object));
			}
		} catch (IOException | ClassNotFoundException e) {
			throw new DatabaseException("ERROR_READING_TRANSACTION_BLOCKS");
		}

		return blocks;
	}

	/**
	 * Read all {@link LogBlock} written using the specified transaction id.
	 * 
	 * @param transactionId
	 * @return the log blocks in the order they were logged
	 */
	protected List<LogBlock> read(long transactionId) {
		return readAll().stream().filter((block) -> block.getTransactionId() == transactionId)
				.collect(Collectors.toList());
	}

	public void shutdown() {
		try {
			ois.close();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createStreams(String logFilePath) {
		try {
			File file = getFileFromPath(logFilePath);

			oos = new ObjectOutputStream(new FileOutputStream(file, true));
			ois = new ObjectInputStream(new FileInputStream(file));
		} catch (IOException e) {
			throw new RuntimeException("Can't create input/output stream to read/log transactions");
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
