package com.bookstore.model;

/** A printed book that needs to be shipped, so it has weight and a shipping cost. */
public class PhysicalBook extends Book {

    private double weightKg;
    private static final double SHIPPING_RATE_PER_KG = 2.50;

    public PhysicalBook(String isbn, String title, String author, double price, int quantity, double weightKg) {
        super(isbn, title, author, price, quantity);
        this.weightKg = weightKg;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    @Override
    public double getExtraCost() {
        return weightKg * SHIPPING_RATE_PER_KG;
    }

    @Override
    public String getFormatLabel() {
        return "PHYSICAL";
    }

    @Override
    public String toCsv() {
        // Reuses Book's toCsv() and appends the extra field. This is
        // polymorphism + code reuse working together: we didn't rewrite
        // the shared fields, only added what's new to this subclass.
        return super.toCsv() + "," + weightKg;
    }
}
