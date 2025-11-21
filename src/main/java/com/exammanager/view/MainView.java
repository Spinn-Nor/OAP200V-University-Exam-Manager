package com.exammanager.view;

import com.exammanager.controller.AppController;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class MainView extends VBox {

    private final TabPane tabPane = new TabPane();

    private final TeacherView teacherView = new TeacherView();
    private final StudentView studentView = new StudentView();

    public MainView() {
        // TODO! add view as second argument to all tabs
        // Creates the individual tabs
        Tab teachersTab = new Tab("Teachers", teacherView);

        Tab studentsTab = new Tab("Students", studentView);

        Tab examsTab = new Tab("Exams");

        // Adds tabs to tabPane
        tabPane.getTabs().addAll(teachersTab, studentsTab, examsTab);

        // Prevents closing tabs in the TabPane
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        getChildren().addAll(tabPane);

        Platform.runLater(() -> new AppController(this).start());
    }

    public TeacherView getTeacherView() {
        return teacherView;
    }

    public StudentView getStudentView() {
        return studentView;
    }
}