package com.exammanager.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;

import java.util.Optional;

/**
 * A utility class for displaying various alerts.
 * <p>
 * Contains methods for displaying:
 * <ul>
 *     <li>Database connection errors</li>
 *     <li>Generic errors with custom messages</li>
 *     <li>Alerts asking for confirmation (e.g. before deletion)</li>
 *     <li>Information about the application</li>
 *     <li>Information dialogs</li>
 * </ul>
 *
 * @author Bendik
 */
public abstract class AlertUtil {

    /**
     * Method for displaying a database connection error.
     * <p>
     * @param message the message to display in the error
     */
    public static void showDatabaseConnectionError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("A database connection error occurred.");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method for displaying a generic error alert.
     * <p>
     * @param header the alert's header text
     * @param message the alert's main content
     */
    public static void genericError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method for displaying an alert asking the user for confirmation.
     * <p>
     * @param title the alert's title
     * @param header the alert's header text
     * @param content the alert's main content
     * @return returns true if 'OK' is clicked, false if 'Cancel' is clicked
     */
    public static boolean confirmationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Method for displaying an information alert containing information
     * about the application. Called when 'About' is clicked under the 'Help' menu
     * of the menu bar.
     */
    public static void showAppInformation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About Exam Manager");
        alert.setContentText(
                "Exam Manager is a simple JavaFX application. \n" +
                "This application is used for managing teachers, students and exams at a university."
        );
        alert.showAndWait();
    }

    /**
     * Method for displaying an information dialog.
     * <p>
     * @param title the dialog's title
     * @param content the dialog's main content text
     */
    public static void showInformation(String title, String content) {
        Dialog<String> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }
}
