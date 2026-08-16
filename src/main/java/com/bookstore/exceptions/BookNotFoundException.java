package com.bookstore.exceptions;

/**
 * A CHECKED exception (extends Exception, not RuntimeException) because
 * "the book wasn't found" is a normal, expected situation that calling
 * code MUST handle - the compiler forces every caller to either catch it
 * or declare it with "throws". This is different from something like
 * IllegalArgumentException (unchecked), which signals a programming
 * mistake rather than an expected business situation.
 */
public class BookNotFoundException extends Exception {

    public BookNotFoundException(String isbn) {
        // super(message) passes the text up to Exception, so it shows up
        // automatically when you call getMessage() or print the exception.
        super("No book found with ISBN: " + isbn);
    }
}
