package com.bookstore.model;

/**
 * OrderItem represents "N copies of this book" inside an Order.
 * We keep this separate from Book itself because an order needs a
 * QUANTITY and a SUBTOTAL that belong to the order, not to the book's
 * permanent record in the catalog.
 */
public class OrderItem {

    private Book book;
    private int quantity;

    public OrderItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * (price + extra cost such as shipping) * quantity.
     * Notice we call book.getExtraCost() here without caring whether book
     * is a PhysicalBook or EBook - POLYMORPHISM means this line works
     * correctly no matter which one it actually is.
     */
    public double getSubtotal() {
        return (book.getPrice() + book.getExtraCost()) * quantity;
    }
}
