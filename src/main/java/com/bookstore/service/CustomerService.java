package com.bookstore.service;

import com.bookstore.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private List<Customer> customers;

    public CustomerService() {
        this.customers = new ArrayList<>();
    }

    public Customer register(String id, String name, String email, String address) {
        Customer customer = new Customer(id, name, email, address);
        customers.add(customer);
        return customer;
    }

    public Customer findById(String id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null; 
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }
}
