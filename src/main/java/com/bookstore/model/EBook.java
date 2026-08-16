package com.bookstore.model;

/** A downloadable book: no shipping cost, but has a file size and download link. */
public class EBook extends Book {

    private double fileSizeMb;
    private String downloadLink;

    public EBook(String isbn, String title, String author, double price, int quantity, double fileSizeMb, String downloadLink) {
        super(isbn, title, author, price, quantity);
        this.fileSizeMb = fileSizeMb;
        this.downloadLink = downloadLink;
    }

    public double getFileSizeMb() {
        return fileSizeMb;
    }

    public void setFileSizeMb(double fileSizeMb) {
        this.fileSizeMb = fileSizeMb;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    @Override
    public double getExtraCost() {
        // No shipping for a digital product.
        return 0.0;
    }

    @Override
    public String getFormatLabel() {
        return "EBOOK";
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + fileSizeMb + "," + downloadLink;
    }
}
