package com.bookstore.controller;

import com.bookstore.exceptions.BookNotFoundException;
import com.bookstore.exceptions.InsufficientStockException;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.EBook;
import com.bookstore.model.Order;
import com.bookstore.model.PhysicalBook;
import com.bookstore.service.BookCatalog;
import com.bookstore.service.CustomerService;
import com.bookstore.service.FileHandler;
import com.bookstore.service.OrderService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BookstoreController implements Initializable {

    // --- Catalog tab ---
    @FXML private TextField searchField;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> colType;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Double> colPrice;
    @FXML private TableColumn<Book, Integer> colQty;
    @FXML private TableColumn<Book, String> colExtra;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField priceField;
    @FXML private TextField qtyField;
    @FXML private TextField extraField;

    // --- Customers & orders tab ---
    @FXML private TextField custIdField;
    @FXML private TextField custNameField;
    @FXML private TextField custEmailField;
    @FXML private TextField custAddressField;
    @FXML private TextField orderCustIdField;
    @FXML private TextField orderIsbnField;
    @FXML private TextField orderQtyField;
    @FXML private Label statusLabel;

    // --- Order history tab ---
    @FXML private ListView<String> orderListView;

    private final BookCatalog catalog = new BookCatalog();
    private final CustomerService customerService = new CustomerService();
    private final OrderService orderService = new OrderService(catalog);
    private final FileHandler fileHandler = new FileHandler("data/books.csv");
    private final ObservableList<Book> displayedBooks = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeCombo.setItems(FXCollections.observableArrayList("PHYSICAL", "EBOOK"));

        colType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFormatLabel()));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colExtra.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getExtraDetail()));

        bookTable.setItems(displayedBooks);

        onLoadBooks();
    }

    @FXML
    private void onAddBook() {
        try {
            String type = typeCombo.getValue();
            String isbn = isbnField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());
            double extra = extraField.getText().isBlank() ? 0.0 : Double.parseDouble(extraField.getText().trim());

            if (type == null || isbn.isEmpty() || title.isEmpty()) {
                showError("Please fill in type, ISBN and title.");
                return;
            }

            Book book;
            if (type.equals("PHYSICAL")) {
                book = new PhysicalBook(isbn, title, author, price, qty, extra);
            } else {
                book = new EBook(isbn, title, author, price, qty, extra, "");
            }
            catalog.addBook(book);
            refreshTable(catalog.getAllBooks());
            clearAddBookFields();
            statusLabel.setText("Added: " + title);

        } catch (NumberFormatException e) {
            showError("Price, quantity and weight/file-size must be numbers.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }
    
    @FXML
    private void onRemoveBook() {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
        showError("Select a book in the table first.");
        return;
        }
        try {
        catalog.removeBook(selected.getIsbn());
        refreshTable(catalog.getAllBooks());
        statusLabel.setText("Removed: " + selected.getTitle());
        } catch (BookNotFoundException e) {
        showError(e.getMessage());
        }
    }

    @FXML
    private void onSearch() {
        String keyword = searchField.getText().trim();
        refreshTable(catalog.searchByTitle(keyword));
    }

    @FXML
    private void onShowAll() {
        refreshTable(catalog.getAllBooks());
    }

    @FXML
    private void onSaveBooks() {
        try {
            fileHandler.saveBooks(catalog.getAllBooks());
            statusLabel.setText("Catalog saved to data/books.csv");
        } catch (IOException e) {
            showError("Could not save file: " + e.getMessage());
        }
    }

    @FXML
    private void onLoadBooks() {
        try {
            List<Book> loaded = fileHandler.loadBooks();
            catalog.getAllBooks().clear();
            for (Book b : loaded) {
                catalog.addBook(b);
            }
            refreshTable(catalog.getAllBooks());
            statusLabel.setText("Loaded " + loaded.size() + " book(s) from file.");
        } catch (IOException e) {
            showError("Could not load file: " + e.getMessage());
        }
    }

    @FXML
    private void onRegisterCustomer() {
        String id = custIdField.getText().trim();
        String name = custNameField.getText().trim();
        String email = custEmailField.getText().trim();
        String address = custAddressField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            showError("Customer ID and name are required.");
            return;
        }
        try {
            customerService.register(id, name, email, address);
            statusLabel.setText("Registered customer: " + name);
            custIdField.clear();
            custNameField.clear();
            custEmailField.clear();
            custAddressField.clear();
        } catch (IllegalArgumentException e) {
            // Thrown by Person.setEmail() if the email has no "@".
            showError(e.getMessage());
        }
    }

    @FXML
    private void onPlaceOrder() {
        String custId = orderCustIdField.getText().trim();
        String isbn = orderIsbnField.getText().trim();

        Customer customer = customerService.findById(custId);
        if (customer == null) {
            showError("No customer with ID: " + custId);
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(orderQtyField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Quantity must be a number.");
            return;
        }

        try {
            Order order = orderService.placeOrder(customer, isbn, qty);
            orderListView.getItems().add(order.toString());
            refreshTable(catalog.getAllBooks());
            statusLabel.setText("Order placed: " + order.getOrderId());
        } catch (BookNotFoundException e) {
            showError(e.getMessage());
        } catch (InsufficientStockException e) {
            showError(e.getMessage());
        }
    }

    private void refreshTable(List<Book> books) {
        displayedBooks.setAll(books);
    }

    private void clearAddBookFields() {
        isbnField.clear();
        titleField.clear();
        authorField.clear();
        priceField.clear();
        qtyField.clear();
        extraField.clear();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }
}
