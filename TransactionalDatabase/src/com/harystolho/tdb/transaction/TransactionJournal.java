package com.harystolho.tdb.transaction;

import java.util.concurrent.atomic.AtomicLong;

public class TransactionJournal {

	private AtomicLong lastTransactionId;

	public TransactionJournal() {
		lastTransactionId = new AtomicLong(0);
	}

	public Transaction createTransaction() {
		return new Transaction(lastTransactionId.getAndIncrement());

	}

	public void beginTransaction(long id) {
		System.out.println("Begin transaction #" + id);
	}

}
