package com.bookstore.exceptions;

public class BookNotFoundException extends Exception {

    public BookNotFoundException(String isbn) {
        super("No book found with ISBN: " + isbn);
    }
}
