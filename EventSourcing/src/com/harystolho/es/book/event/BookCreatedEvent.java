package com.harystolho.es.book.event;

import com.harystolho.es.DependecyInjector;
import com.harystolho.es.book.Book;
import com.harystolho.es.book.BookRepository;
import com.harystolho.es.event.Event;

public class BookCreatedEvent extends Event {

	private final String isbn;
	private final String title;
	private final String author;

	public BookCreatedEvent(String isbn, String title, String author) {
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
	public String normalize() {
		return String.format("isbn=%s,title=\"%s\",author=\"%s\"", isbn, title, author);
	}

}
