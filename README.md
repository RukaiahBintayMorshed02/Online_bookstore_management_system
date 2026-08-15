# Online Bookstore Management System

## Requirements
- JDK 17 or later
- Maven (or use your IDE's built-in Maven support - IntelliJ and Eclipse both have this)
- Internet access the first time you build (Maven needs to download the JavaFX libraries)

## How to run

### Option A - command line
```
mvn clean javafx:run
```

### Option B - IntelliJ IDEA
1. Open the `bookstore` folder as a project (File > Open, select the folder with `pom.xml`).
2. Let Maven finish downloading dependencies (bottom-right progress bar).
3. Right-click `Main.java` > Run 'Main.main()'.
   - If you get a "JavaFX runtime components are missing" error, go to
     Run > Edit Configurations > VM options, and add:
     `--module-path <path-to-javafx-lib> --add-modules javafx.controls,javafx.fxml`
   - This is usually only needed if you're NOT using `mvn javafx:run`, because
     the Maven plugin sets this up for you automatically.

### Option C — Eclipse
Import as an existing Maven project (File > Import > Maven > Existing Maven Projects),
then run `Main.java` the same way, or use the Maven build (right-click pom.xml >
Run As > Maven build... > goal: `javafx:run`).

## Project structure
```
src/main/java/com/bookstore/
  Main.java                     - JavaFX entry point
  model/                        - data classes (Person, Customer, Book, PhysicalBook,
                                   EBook, OrderItem, Order)
  exceptions/                   - custom checked exceptions
  service/                      - business logic (BookCatalog, OrderService,
                                   CustomerService, FileHandler)
  controller/                   - JavaFX controller connecting UI to services
src/main/resources/com/bookstore/
  bookstore.fxml                - UI layout
data/
  books.csv                     - saved catalog data (created after you click
                                   "Save catalog to file")
```

## Using the app
1. **Catalog tab** — add books (pick PHYSICAL or EBOOK from the dropdown, fill in the
   fields, the last field is weight in kg for physical books or file size in MB for
   e-books). Search by title. Save/load the catalog to `data/books.csv`.
2. **Customers & orders tab** — register a customer with an ID, then place an order
   using that customer ID and a book's ISBN. Try ordering more copies than are in
   stock, or an ISBN that doesn't exist, to see the exception handling in action.
3. **Order history tab** — shows every order placed this session.
