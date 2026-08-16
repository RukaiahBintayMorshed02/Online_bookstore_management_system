package com.bookstore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
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
