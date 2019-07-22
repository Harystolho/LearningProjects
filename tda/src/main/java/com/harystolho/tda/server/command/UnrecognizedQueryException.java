package com.harystolho.tda.server.command;

public class UnrecognizedQueryException extends RuntimeException {

	private static final long serialVersionUID = 866574275477L;

	public UnrecognizedQueryException() {
		super();
	}

	public UnrecognizedQueryException(String message) {
		super(message);
	}

}
