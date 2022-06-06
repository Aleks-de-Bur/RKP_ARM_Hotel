package com.hotel.arm_hotel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ARMHotel extends Application {
    private static Stage primaryStage;
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    private static Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ARM_Hotel";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "masterkey";

    public static Connection getConnection() {
        return connection;
    }

    @Override
    public void stop() throws Exception {
        connection.close();
        super.stop();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = primaryStage;
        try{
            connection = DriverManager.getConnection(DB_URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось подключиться к базе данных.", ButtonType.OK);
            alert.showAndWait();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(ARMHotel.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 600);
        stage.setTitle("ARM_Hotel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}