package com.harystolho.es.book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookRepository {

	// isbn -> Book
	private Map<String, Book> books;

	public BookRepository() {
		this.books = new HashMap<>();
	}

	public void saveBook(Book book) {
		books.put(book.getIsbn(), book);
	}

	public Book getBookByISBN(String isbn) {
		Book book = books.get(isbn);

		if (book == null)
			return null;

		return book.clone();
	}

	public Book getBookByTitle(String title) {
		Optional<Book> book = books.values().stream().filter((b) -> b.getTitle().equals(title)).findFirst();

		if (!book.isPresent())
			return null;

		return book.get().clone();
	}

	public void reset() {
		books.clear();
	}

	public List<Book> getAll() {
		return new ArrayList<>(books.values());
	}

}
