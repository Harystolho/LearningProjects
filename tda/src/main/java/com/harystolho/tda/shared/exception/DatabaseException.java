package com.harystolho.tda.shared.exception;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 3872175775L;

	public DatabaseException() {
		super();
	}

	public DatabaseException(String message) {
		super(message);
	}

}
