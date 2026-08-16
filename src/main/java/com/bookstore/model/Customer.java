package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Customer IS-A Person (INHERITANCE: "extends Person"). It automatically
 * gets id, name, email, and their getters/setters for free, and only needs
 * to add what's unique to a customer: an address and their order history.
 */
public class Customer extends Person {

    private String address;
    private List<Order> orderHistory;

    public Customer(String id, String name, String email, String address) {
        // super(...) calls Person's constructor to set up the shared fields.
        super(id, name, email);
        this.address = address;
        this.orderHistory = new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    /**
     * This is POLYMORPHISM in action: Person declared describe() as
     * abstract, and here Customer provides its own concrete version.
     * Anywhere in the code that holds a "Person" reference pointing at a
     * Customer object, calling describe() runs THIS method automatically.
     */
    @Override
    public String describe() {
        return "Customer: " + getName() + " (" + getEmail() + "), " + orderHistory.size() + " past order(s)";
    }

    @Override
    public String toString() {
        return getId() + "," + getName() + "," + getEmail() + "," + address;
    }
}
