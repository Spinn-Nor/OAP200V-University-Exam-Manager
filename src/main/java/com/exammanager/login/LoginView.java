package com.exammanager.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * @author Samah & Rasha
 */
public class LoginView extends VBox {

    private TextField emailField;
    private TextField passwordField;
    private Button loginButton;

    public LoginView() {
        // ----------------------------
        // Title
        // ----------------------------
        Label title = new Label("Exam Manager");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // ----------------------------
        // Fields
        // ----------------------------
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(
                "-fx-background-radius: 10; -fx-padding: 10; -fx-border-radius: 10; -fx-border-color: #cccccc;"
        );

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(
                "-fx-background-radius: 10; -fx-padding: 10; -fx-border-radius: 10; -fx-border-color: #cccccc;"
        );

        // ----------------------------
        // Login Button
        // ----------------------------
        loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-background-color: #4a90e2; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10 20;"
        );

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

        // ----------------------------
        // Form container
        // ----------------------------
        VBox formBox = new VBox(15, title, emailField, passwordField, loginButton, errorLabel);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setPrefWidth(600);
        formBox.setPrefHeight(400);
        formBox.setStyle("-fx-background-color: white;");

        // ----------------------------
        // Background container
        // ----------------------------
        StackPane root = new StackPane(formBox);
        root.setPadding(new Insets(40));

        // nice gradient background
        BackgroundFill bgFill = new BackgroundFill(
                Color.web("#cfe0fc"), CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(bgFill));

        // ----------------------------
        // Login action
        // ----------------------------
        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();


        });

        getChildren().add(formBox);
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}