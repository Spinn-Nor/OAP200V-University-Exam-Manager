package com.exammanager.util;

import javafx.application.Platform;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

    private static final String PROPERTIES_FILE = "/db.properties";

    public static Connection getConnection() {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (in == null) {
                showError("Properties file not found");
                return null;
            }

            Properties prop = new Properties();
            prop.load(in);

            String url = prop.getProperty("db.url");
            String username = prop.getProperty("db.username");
            String password = prop.getProperty("db.password");

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            showError("Failed to connect to database.\n" + e.getMessage());
            return null;
        }
    }

    private static void showError(String message) {
        Platform.runLater(() -> AlertUtil.showDatabaseConnectionError(message));
    }
}
