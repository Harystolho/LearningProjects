package com.harystolho.es.book;

import com.harystolho.es.book.exception.BookReservedExeception;

public class Book implements Cloneable {

	private String isbn;
	private String title;
	private String author;
	private boolean booked;

	public Book(String isbn, String title, String author) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.booked = false;
	}

	public void reserve() {
		if (this.booked)
			throw new BookReservedExeception(String.format("This book is already reserved. [isbn: %s]", isbn));

		this.booked = true;
	}

	public void unreserve() {
		this.booked = false;
	}

	public String getTitle() {
		return title;
	}

	public String getIsbn() {
		return isbn;
	}

	@Override
	protected Book clone() {
		Book other = new Book(isbn, title, author);
		other.booked = this.booked;

		return other;
	}

}
