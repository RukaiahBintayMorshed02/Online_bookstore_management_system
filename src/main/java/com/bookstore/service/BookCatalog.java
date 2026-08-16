package com.bookstore.service;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * BookCatalog is a "manager" class: it owns the master List<Book> and is
 * the ONLY place in the program allowed to add/remove/search books
 * directly. Every other class (the controller, OrderService) goes through
 * this class instead of touching a raw List itself - this keeps the rules
 * about the catalog (e.g. "ISBN must be unique") enforced in one place.
 */
public class BookCatalog {

    private List<Book> books;

    public BookCatalog() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(String isbn) throws BookNotFoundException {
        Book book = findByIsbn(isbn); // throws if not found, so we stop here on failure
        books.remove(book);
    }

    /**
     * Throws a CHECKED exception instead of returning null. This forces
     * every caller (OrderService, the controller) to explicitly deal with
     * "book might not exist" rather than accidentally causing a
     * NullPointerException three lines later.
     */
    public Book findByIsbn(String isbn) throws BookNotFoundException {
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                return b;
            }
        }
        throw new BookNotFoundException(isbn);
    }

    /** Simple case-insensitive partial match search used by the UI search box. */
    public List<Book> searchByTitle(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(b);
            }
        }
        return results;
    }

    public void updateStock(String isbn, int newQuantity) throws BookNotFoundException {
        Book book = findByIsbn(isbn);
        book.setQuantity(newQuantity);
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public int size() {
        return books.size();
    }
}
