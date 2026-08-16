package com.bookstore.model;

public abstract class Book {

    private String isbn;
    private String title;
    private String author;
    private double price;
    private int quantity;

    public Book(String isbn, String title, String author, double price, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public abstract double getExtraCost();

    public abstract String getFormatLabel();

    public abstract String getExtraDetail();
    
    public String toCsv() {
        return getFormatLabel() + "," + isbn + "," + title + "," + author + "," + price + "," + quantity;
    }
}
