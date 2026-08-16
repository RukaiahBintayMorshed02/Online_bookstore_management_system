package com.bookstore.service;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookCatalog {

    private List<Book> books;

    public BookCatalog() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(String isbn) throws BookNotFoundException {
        Book book = findByIsbn(isbn); // throws if not found
        books.remove(book);
    }

    public Book findByIsbn(String isbn) throws BookNotFoundException {
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                return b;
            }
        }
        throw new BookNotFoundException(isbn);
    }

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
