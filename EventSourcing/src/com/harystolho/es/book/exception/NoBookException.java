package com.harystolho.es.book.exception;

public class NoBookException extends RuntimeException {

	public NoBookException(String field, String value) {
		super(String.format("There is no book with the specified %s [%s: %s]", field, field, value));
	}

}
