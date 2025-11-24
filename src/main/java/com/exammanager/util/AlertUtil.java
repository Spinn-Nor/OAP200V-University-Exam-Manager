package com.exammanager.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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

    public static boolean confirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
