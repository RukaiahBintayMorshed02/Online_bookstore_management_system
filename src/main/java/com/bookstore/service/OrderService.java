package com.bookstore.service;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;

import java.util.ArrayList;
import java.util.List;


public class OrderService {

    private BookCatalog catalog;
    private List<Order> orders;
    private int nextOrderNumber = 1;

    public OrderService(BookCatalog catalog) {
        this.catalog = catalog;
        this.orders = new ArrayList<>();
    }

    public Order placeOrder(Customer customer, String isbn, int quantity)
            throws BookNotFoundException, InsufficientStockException {

        Book book = catalog.findByIsbn(isbn); // may throw BookNotFoundException

        if (book.getQuantity() < quantity) {
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
