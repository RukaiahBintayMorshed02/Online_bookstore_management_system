package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {

    private String address;
    private List<Order> orderHistory;

    public Customer(String id, String name, String email, String address) {
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

    @Override
    public String describe() {
        return "Customer: " + getName() + " (" + getEmail() + "), " + orderHistory.size() + " past order(s)";
    }

    @Override
    public String toString() {
        return getId() + "," + getName() + "," + getEmail() + "," + address;
    }
}
