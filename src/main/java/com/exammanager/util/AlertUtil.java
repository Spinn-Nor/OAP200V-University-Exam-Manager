package com.exammanager.util;

import javafx.scene.control.Alert;

public class AlertUtil {

    public static void showDatabaseConnectionError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("A database connection error occurred.");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void genericError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
