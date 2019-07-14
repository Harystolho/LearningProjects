package com.harystolho.tdb;

import com.harystolho.tdb.transaction.Transaction;
import com.harystolho.tdb.transaction.TransactionJournal;

public class Database {

	private String name;

	private final TransactionJournal transactionJournal;

	private Database(String name) {
		this.name = name;
		transactionJournal = new TransactionJournal();
	}

	public static Database create(String name) {
		System.out.println(String.format("Creating database '%s'", name));

		return new Database(name);
	}

	public Transaction startTransaction() {
		Transaction transaction = transactionJournal.createTransaction();

		transactionJournal.beginTransaction(transaction.getId());

		return transaction;
	}

}
