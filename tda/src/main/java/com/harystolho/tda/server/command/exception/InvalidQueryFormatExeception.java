package com.harystolho.tda.server.command.exception;

public class InvalidQueryFormatExeception extends RuntimeException {

	private static final long serialVersionUID = 457273579542L;

	public InvalidQueryFormatExeception() {
		super();
	}

	public InvalidQueryFormatExeception(String message) {
		super(message);
	}

}
