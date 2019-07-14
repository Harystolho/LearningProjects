package com.harystolho.tdb.client;

import com.harystolho.tdb.Transaction;

public interface Connection {

	public Transaction startTransaction();

}
