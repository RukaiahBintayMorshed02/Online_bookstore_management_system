package com.bookstore.model;

public class PhysicalBook extends Book {

    private double weightKg;
    private static final double SHIPPING_RATE_PER_KG = 50.0;

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
    public String getExtraDetail() {
    return weightKg + " kg";
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + weightKg;
    }
}
