package com.exammanager.service;

import com.exammanager.login.AccessLevel;
import com.exammanager.util.AlertUtil;
import com.exammanager.util.DatabaseConnection;
import com.exammanager.util.PasswordCryptography;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public abstract class ManageUserService {

    private static TextField emailField;
    private static PasswordField passwordField;
    private static PasswordField confirmPasswordField;
    private static ButtonType saveButton;

    public static void addUser() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return;
        }

        Optional<UserDetails> dialogResult = addUserDialog();
        if (dialogResult.isEmpty()) {
            return;
        }

        String salt = PasswordCryptography.generateSalt();
        System.out.println(salt);

        String hash = PasswordCryptography.hashPassword(dialogResult.get().password, salt);
        System.out.println(hash);

        String sql = "INSERT INTO user (email, hash, salt, access_level) VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dialogResult.get().email);
            stmt.setString(2, hash);
            stmt.setString(3, salt);
            stmt.setString(4, dialogResult.get().accessLevel.name());
            stmt.executeUpdate();
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while adding user: " + e.getMessage());
            return;
        }

        AlertUtil.showInformation("User Added", "User '" + dialogResult.get().email + "' added successfully.");
    }

    // TODO!
    public static void deleteUser(String currentUserEmail) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return;
        }

        String sql = "SELECT email FROM user;";

        ObservableList<String> users = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(rs.getString("email"));
            }
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while deleting user. Could not retrieve users: " + e.getMessage());
            return;
        }

        Optional<String> dialogResult = deleteUserDialog(users);
        if (dialogResult.isEmpty()) {
            return;
        }

        if (currentUserEmail.equals(dialogResult.get())) {
            AlertUtil.genericError("Error", "Cannot delete your own user.");
            return;
        }

        sql =  "DELETE FROM user WHERE email = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dialogResult.get());
            stmt.executeUpdate();
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while deleting user: " + e.getMessage());
            return;
        }

        AlertUtil.showInformation("User Deleted", "User '" + dialogResult.get() + "' deleted successfully.");
    }

    private static Optional<UserDetails> addUserDialog() {
        Dialog<UserDetails> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setHeight(300);
        dialog.setWidth(400);
        dialog.setTitle("Add User");
        dialog.setHeaderText("Add user");
        dialog.resizableProperty().setValue(false);

        GridPane content = new GridPane();
        content.setHgap(10);
        content.setVgap(10);
        content.setAlignment(Pos.CENTER);

        Label emailLabel = new Label("Email: ");
        content.add(emailLabel, 0, 0);

        emailField = new TextField();
        content.add(emailField, 1, 0, 4, 1);

        Label passwordLabel = new Label("Password: ");
        content.add(passwordLabel, 0, 1);

        passwordField = new PasswordField();
        content.add(passwordField, 1, 1, 4, 1);

        Label confirmPasswordLabel = new Label("Confirm password: ");
        content.add(confirmPasswordLabel, 0, 2);

        confirmPasswordField = new PasswordField();
        content.add(confirmPasswordField, 1, 2, 4, 1);

        Label accessLevelLabel = new Label("Account type: ");
        content.add(accessLevelLabel, 0, 3);

        ComboBox<AccessLevel> accessLevelComboBox = new ComboBox<>();
        accessLevelComboBox.getItems().addAll(AccessLevel.values());
        accessLevelComboBox.setValue(AccessLevel.STUDENT);
        accessLevelComboBox.setPrefWidth(200);
        content.add(accessLevelComboBox, 1, 3, 4, 1);

        ButtonType closeButton = new ButtonType("Close", ButtonType.CLOSE.getButtonData());
        saveButton = new ButtonType("Add", ButtonType.APPLY.getButtonData());

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(closeButton, saveButton);
        dialog.getDialogPane().lookupButton(saveButton).disableProperty().setValue(true);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                return new UserDetails(
                        emailField.getText().trim(),
                        passwordField.getText(),
                        accessLevelComboBox.getValue()
                );
            } else {
                return null;
            }
        });

        dialog.getDialogPane().lookupButton(saveButton).addEventFilter(ActionEvent.ACTION, event -> {
            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                AlertUtil.genericError("Passwords not matching", "Passwords do not match!");
                event.consume();
            } else {
                dialog.close();
            }
        });

        Runnable updateButtonState = () -> {
            boolean emptyField =
                    emailField.getText().trim().isEmpty() ||
                    passwordField.getText().isEmpty() ||
                    confirmPasswordField.getText().isEmpty();

            dialog.getDialogPane().lookupButton(saveButton).setDisable(emptyField);
        };

        ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> updateButtonState.run();

        emailField.textProperty().addListener(textFieldListener);
        passwordField.textProperty().addListener(textFieldListener);
        confirmPasswordField.textProperty().addListener(textFieldListener);

        return dialog.showAndWait();
    }

    private static class UserDetails {
        String email;
        String password;
        AccessLevel accessLevel;

        public UserDetails(String email, String password, AccessLevel accessLevel) {
            this.email = email;
            this.password = password;
            this.accessLevel = accessLevel;
        }
    }

    private static Optional<String> deleteUserDialog(ObservableList<String> users) {
        Dialog<String> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setHeight(200);
        dialog.setWidth(300);
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Select the user you want to delete.");
        dialog.resizableProperty().setValue(false);

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);

        Label courseLabel = new Label("User: ");
        content.getChildren().add(courseLabel);

        ComboBox<String> userComboBox = new ComboBox<>(users);

        ButtonType closeButton = new ButtonType("Close", ButtonType.CLOSE.getButtonData());
        ButtonType saveButton = new ButtonType("Delete", ButtonType.APPLY.getButtonData());

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(closeButton, saveButton);
        dialog.getDialogPane().lookupButton(saveButton).disableProperty().setValue(true);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                return userComboBox.getValue();
            } else {
                return null;
            }
        });

        userComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dialog.getDialogPane().lookupButton(saveButton).disableProperty().setValue(false);
            }
        });

        dialog.getDialogPane().lookupButton(saveButton).addEventFilter(ActionEvent.ACTION, event -> {
            if (AlertUtil.confirmationAlert(
                    "Confirm Delete",
                    "Confirm deletion",
                    "Are you sure you want to delete '" + userComboBox.getValue() + "'?"
            )) {
                dialog.close();
            } else {
                event.consume();
            }
        });

        return dialog.showAndWait();
    }

}
