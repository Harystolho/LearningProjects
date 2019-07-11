package com.harystolho.es;

public class Main {

	public static void main(String[] args) {
		LibraryService libraryService = new LibraryService();

		libraryService.addBook("ab142", "The long life", "Chuck Norris");
		libraryService.addBook("he757", "Pretty Land", "Evan Sky");
		libraryService.addBook("ih391", "Happy Code", "Martin Follower");

		libraryService.reserveBook("Pretty Land");
		libraryService.reserveBook("Happy Code");

		libraryService.returnBook("he757");

		libraryService.reset();

		System.out.println("Listing library books: " + libraryService.listBooks());

		libraryService.restore();

		System.out.println("Listing library books: " + libraryService.listBooks());
	}

}
