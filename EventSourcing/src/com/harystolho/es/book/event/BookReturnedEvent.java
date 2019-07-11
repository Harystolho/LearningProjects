package com.harystolho.es.book.event;

import java.util.HashMap;
import java.util.Map;

import com.harystolho.es.DependecyInjector;
import com.harystolho.es.book.Book;
import com.harystolho.es.book.BookRepository;
import com.harystolho.es.book.exception.NoBookException;
import com.harystolho.es.event.Event;

public class BookReturnedEvent extends Event {

	private String isbn;

	public BookReturnedEvent(String isbn) {
		super("BOOK_RETURNED_EVENT");

		this.isbn = isbn;
	}

	public String getIsbn() {
		return isbn;
	}

	@Override
	public void process() {
		BookRepository bookAggregate = DependecyInjector.getBookaggregate();

		Book book = bookAggregate.getBookByISBN(getIsbn());

		if (book == null)
			throw new NoBookException("isbn", getIsbn());

		book.unreserve();

		bookAggregate.saveBook(book);
	}

	@Override
	public Map<String, String> normalize() {
		Map<String, String> map = new HashMap<>();

		map.put("isbn", isbn);

		return map;
	}

}
