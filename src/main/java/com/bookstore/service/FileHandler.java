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

/**
 * FileHandler is responsible for ALL disk access in the program - no other
 * class opens a file directly. Centralising this means:
 *   1. If we ever change the file format, we only edit this one class.
 *   2. Every possible I/O failure (missing file, permissions, corrupted
 *      line) is caught and handled in exactly one place instead of being
 *      scattered around the codebase.
 *
 * We store books as plain CSV text so the file is human-readable, which
 * makes it easy to demonstrate/debug during a viva.
 */
public class FileHandler {

    private String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves every book to disk as one CSV line each.
     * try-with-resources (the "try (BufferedWriter writer = ...)" syntax)
     * guarantees the file is closed automatically even if an exception is
     * thrown partway through writing - we never need a manual finally
     * block just to close the stream.
     */
    public void saveBooks(List<Book> books) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                writer.write(book.toCsv());
                writer.newLine();
            }
        }
        // We deliberately do NOT catch IOException here. This method's job
        // is only to WRITE; deciding what to tell the user belongs to the
        // caller (the controller), so we let the exception propagate up by
        // declaring "throws IOException" in the method signature.
    }

    /**
     * Loads books back from disk. Every line is parsed defensively: if one
     * line is corrupted we skip it and keep going rather than letting the
     * whole load fail, but we still report which lines failed.
     */
    public List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            // Not an error - just means this is the first run and there's
            // nothing to load yet. Return an empty list instead of throwing.
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
                    // A single bad line shouldn't crash the whole load.
                    // We report it and move on to the next line - this is
                    // "graceful degradation" instead of an all-or-nothing
                    // failure.
                    System.err.println("Skipping malformed line " + lineNumber + ": " + malformedLine.getMessage());
                }
            }
        }
        return books;
    }

    /**
     * Turns one CSV line back into the correct Book subclass.
     * This throws an UNCHECKED IllegalArgumentException on bad data
     * (missing fields, bad numbers) because a malformed line is a data
     * problem, not something the caller is expected to plan a recovery
     * strategy around beyond "skip it" - which loadBooks() already does.
     */
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
            // We CATCH the low-level NumberFormatException here and
            // re-throw a clearer, higher-level exception. This is
            // "exception translation": the caller of parseBookLine()
            // doesn't need to know or care that Double.parseDouble()
            // was involved - it just needs to know the line was bad.
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
