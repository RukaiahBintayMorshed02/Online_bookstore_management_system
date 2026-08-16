package com.bookstore.exceptions;

public class InsufficientStockException extends Exception {

    public InsufficientStockException(String title, int requested, int available) {
        super("Cannot order " + requested + " copies of \"" + title + "\" - only " + available + " in stock");
    }
}
