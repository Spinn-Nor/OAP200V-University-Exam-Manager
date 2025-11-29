package com.exammanager.util;

import javafx.application.Platform;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * A class for handling database connections.
 * <p>
 * Contains a method for getting a database connection. User properties
 * for a separate properties file to establish a connection to a MySQL-database.
 *
 * @author Bendik
 */
public abstract class DatabaseConnection {

    private static final String PROPERTIES_FILE = "/db.properties";

    /**
     * A method for getting a connection to a MySQL-database.
     *
     * @return {@link Connection} returns a database connection
     */
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
