package com.bookstore.service;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderService coordinates BookCatalog and Customer data to build an Order.
 * This is a good example of classes COLLABORATING rather than one giant
 * class doing everything - BookCatalog owns book data, OrderService owns
 * the rules for placing an order, and it borrows book lookups from
 * BookCatalog instead of duplicating that logic.
 */
public class OrderService {

    private BookCatalog catalog;
    private List<Order> orders;
    private int nextOrderNumber = 1;

    public OrderService(BookCatalog catalog) {
        this.catalog = catalog;
        this.orders = new ArrayList<>();
    }

    /**
     * Places an order for one book. Declares "throws" for BOTH exceptions
     * because both are checked exceptions and this method doesn't handle
     * them itself - it passes the responsibility up to whoever called
     * placeOrder(), which in our case is the JavaFX controller, where we
     * catch them and show the user a friendly error message.
     */
    public Order placeOrder(Customer customer, String isbn, int quantity)
            throws BookNotFoundException, InsufficientStockException {

        Book book = catalog.findByIsbn(isbn); // may throw BookNotFoundException

        if (book.getQuantity() < quantity) {
            // We deliberately check stock BEFORE changing anything, so that
            // if this throws, no partial/incorrect state has been saved.
            throw new InsufficientStockException(book.getTitle(), quantity, book.getQuantity());
        }

        Order order = new Order("ORD-" + String.format("%04d", nextOrderNumber++), customer);
        order.addItem(new OrderItem(book, quantity));

        // Reduce stock now that we know the order is valid.
        book.setQuantity(book.getQuantity() - quantity);

        orders.add(order);
        customer.addOrder(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
