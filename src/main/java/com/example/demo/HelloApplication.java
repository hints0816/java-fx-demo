package com.example.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();

        showPersonOverview();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Order> orderObservableList = FXCollections.observableArrayList();

    public void showPersonOverview() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(personOverview);

            HelloController controller = loader.getController();
            controller.setMainApp();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public HelloApplication() {
        // Add some sample data
        orderObservableList.add(new Order("Hans", "Muster"));
        orderObservableList.add(new Order("Ruth", "Mueller"));
        orderObservableList.add(new Order("Heinz", "Kurz"));
        orderObservableList.add(new Order("Cornelia", "Meier"));
        orderObservableList.add(new Order("Werner", "Meyer"));
        orderObservableList.add(new Order("Lydia", "Kunz"));
        orderObservableList.add(new Order("Anna", "Best"));
        orderObservableList.add(new Order("Stefan", "Meier"));
        orderObservableList.add(new Order("Martin", "Mueller"));
    }

    public ObservableList<Order> getOrderObservableList() {
        return orderObservableList;
    }

    public static void main(String[] args) {
        launch(args);
    }
}