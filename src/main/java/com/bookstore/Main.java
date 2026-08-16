package com.bookstore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main is the entry point of the JavaFX app. It extends Application, and
 * JavaFX requires us to implement start(Stage). Everything begins here:
 *   1. main() calls launch(), which is inherited from Application and
 *      sets up the JavaFX runtime (this must happen before any JavaFX
 *      objects like Stage/Scene can be created).
 *   2. JavaFX then calls start(Stage) for us automatically.
 *   3. We load bookstore.fxml (the layout) and attach it to the window.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // FXMLLoader reads the .fxml file and builds the actual UI
        // components (buttons, tables, etc.) described in it, and wires
        // up the BookstoreController class named inside that file.
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("bookstore.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Online Bookstore");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Hands control over to the JavaFX runtime.
    }
}
