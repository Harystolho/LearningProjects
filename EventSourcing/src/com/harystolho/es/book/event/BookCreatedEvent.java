package com.harystolho.es.book.event;

import java.util.HashMap;
import java.util.Map;

import com.harystolho.es.DependecyInjector;
import com.harystolho.es.book.Book;
import com.harystolho.es.book.BookRepository;
import com.harystolho.es.event.Event;

public class BookCreatedEvent extends Event {

	private final String isbn;
	private final String title;
	private final String author;

	public BookCreatedEvent(String isbn, String title, String author) {
		super("BOOK_CREATED_EVENT");
		
		this.isbn = isbn;
		this.title = title;
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	@Override
	public void process() {
		BookRepository bookAggregate = DependecyInjector.getBookaggregate();

		bookAggregate.saveBook(new Book(isbn, title, author));
	}

	@Override
	public Map<String, String> normalize() {
		Map<String, String> map = new HashMap<>();
		
		map.put("isbn", isbn);
		map.put("title", title);
		map.put("author", author);
		
		return map;
	}

}
