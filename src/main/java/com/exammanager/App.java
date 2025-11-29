package com.exammanager;

import com.exammanager.login.AccessLevel;
import com.exammanager.login.LoginAuth;
import com.exammanager.util.AlertUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.exammanager.view.MainView;

import com.exammanager.login.LoginView;

import java.util.Optional;

// TODO! UPDATE DOCUMENTATION WHEN LOGIN IS ADDED

/**
 * Entry point for the Exam Manager application.
 * <p>
 * This class extends {@link Application} and launches a JavaFX GUI
 * for managing teachers, students and exams at a university.
 * It initializes the main view, sets up the application window (scene), and displays
 * the scene to the user.
 *
 * @author Bendik
 * @version 1.0
 */
public class App extends Application {

    private MainView mainView;

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView();
        Scene loginScene = new Scene(loginView, 600, 400);

        stage.setTitle("Exam Manager");
        stage.setScene(loginScene);
        stage.show();

        loginView.getLoginButton().setOnAction(e -> {
            String email = loginView.getEmailField().getText();
            String password = loginView.getPasswordField().getText();
            // Optional<AccessLevel> loginAuthResult = LoginAuth.loginAuth(email, password);

            // FIXME! FOR TESTING
            var loginAuthResult = loginTeacher();

            loginAuthResult.ifPresent(accessLevel -> switchToMainView(stage, accessLevel));
        });
    }

    public static void main(String[] args) {
        launch();
    }

    private void switchToMainView(Stage stage, AccessLevel accessLevel) {
        // Hide stage before switching scene to avoid window flickering
        stage.hide();

        // Setup MainView with all associated views after logging in, set and show scene centered on screen
        mainView = new MainView(accessLevel);
        Scene mainScene = new Scene(mainView, 1280, 720);
        stage.setScene(mainScene);
        stage.getScene().getWindow().centerOnScreen();
        stage.show();
    }

    // TODO! METHODS SIMULATING SUCCESSFUL LOGIN FOR TESTING
    private static Optional<AccessLevel> loginAdmin() {
        return Optional.of(AccessLevel.ADMIN);
    }

    private static Optional<AccessLevel> loginTeacher() {
        return Optional.of(AccessLevel.TEACHER);
    }

    private static Optional<AccessLevel> loginStudent() {
        return Optional.of(AccessLevel.STUDENT);
    }

}
