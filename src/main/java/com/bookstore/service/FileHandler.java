package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.EBook;
import com.bookstore.model.PhysicalBook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    public void saveBooks(List<Book> books) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                writer.write(book.toCsv());
                writer.newLine();
            }
        }
    }

    public List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                try {
                    books.add(parseBookLine(line));
                } catch (IllegalArgumentException malformedLine) {
                    System.err.println("Skipping malformed line " + lineNumber + ": " + malformedLine.getMessage());
                }
            }
        }
        return books;
    }
    
    private Book parseBookLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("expected at least 6 fields, got " + parts.length);
        }

        String type = parts[0];
        String isbn = parts[1];
        String title = parts[2];
        String author = parts[3];

        double price;
        int quantity;
        try {
            price = Double.parseDouble(parts[4]);
            quantity = Integer.parseInt(parts[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid number in line: " + line, e);
        }

        if (type.equals("PHYSICAL")) {
            double weight = parts.length > 6 ? Double.parseDouble(parts[6]) : 0.0;
            return new PhysicalBook(isbn, title, author, price, quantity, weight);
        } else if (type.equals("EBOOK")) {
            double fileSize = parts.length > 6 ? Double.parseDouble(parts[6]) : 0.0;
            String link = parts.length > 7 ? parts[7] : "";
            return new EBook(isbn, title, author, price, quantity, fileSize, link);
        } else {
            throw new IllegalArgumentException("unknown book type: " + type);
        }
    }
}
