package com.bookstore.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private String orderId;
    private Customer customer;
    private List<OrderItem> items;
    private LocalDate orderDate;
    private String status; // "PLACED", "SHIPPED", "DELIVERED", "CANCELLED"

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.orderDate = LocalDate.now();
        this.status = "PLACED";
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Order " + orderId + " | " + customer.getName() + " | Tk " + String.format("%.2f", getTotalAmount()) + " | " + status;
    }
}
