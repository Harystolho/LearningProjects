package com.harystolho.tdb_shared.exception;

public class RollbackTransactionException extends RuntimeException {

	private static final long serialVersionUID = 63457897897L;

	public RollbackTransactionException() {
		super();
	}

	public RollbackTransactionException(String message) {
		super(message);
	}

	public RollbackTransactionException(String message, Throwable t) {
		super(message, t);
	}

}
