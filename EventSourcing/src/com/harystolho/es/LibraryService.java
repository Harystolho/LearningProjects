package com.harystolho.es;

import java.util.List;

import com.harystolho.es.book.Book;
import com.harystolho.es.book.BookRepository;
import com.harystolho.es.book.event.BookCreatedEvent;
import com.harystolho.es.book.event.BookReservedEvent;
import com.harystolho.es.book.event.BookReturnedEvent;
import com.harystolho.es.book.exception.NoBookException;
import com.harystolho.es.event.EventProcessor;

public class LibraryService {

	private BookRepository bookAggregate;
	private EventProcessor eventProcessor;

	public LibraryService() {
		eventProcessor = new EventProcessor();
		bookAggregate = DependecyInjector.getBookaggregate();
	}

	public void addBook(String isbn, String title, String author) {
		eventProcessor.process(new BookCreatedEvent(isbn, title, author));
	}

	public void reserveBook(String bookTitle) {
		Book book = bookAggregate.getBookByTitle(bookTitle);

		if (book == null)
			throw new NoBookException("title", bookTitle);

		eventProcessor.process(new BookReservedEvent(book.getIsbn()));
	}

	public void returnBook(String isbn) {
		eventProcessor.process(new BookReturnedEvent(isbn));
	}

	public List<Book> listBooks() {
		return bookAggregate.getAll();
	}

	public void reset() {
		bookAggregate.reset();
	}

	public void restore() {
		eventProcessor.restore();
	}

}
