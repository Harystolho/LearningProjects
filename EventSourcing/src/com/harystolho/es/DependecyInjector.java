package com.harystolho.es;

import com.harystolho.es.book.BookRepository;

public class DependecyInjector {

	private static final BookRepository bookAggregate;

	static {
		bookAggregate = new BookRepository();
	}

	public static BookRepository getBookaggregate() {
		return bookAggregate;
	}

}
