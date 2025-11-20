package com.exammanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.exammanager.view.MainView;

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

    @Override
    public void start(Stage stage) {
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView, 1280, 720);

        stage.setTitle("Exam Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}