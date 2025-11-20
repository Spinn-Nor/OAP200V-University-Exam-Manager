package com.exammanager.util;

import javafx.scene.control.Alert;

/**
 * A utility class for displaying error messages.
 * <p>
 * Contains methods for displaying errors for:
 * <ul>
 *     <li>Database connection errors</li>
 *     <li>Generic errors with custom messages</li>
 * </ul>
 *
 * @author Bendik
 */
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
