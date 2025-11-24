package com.exammanager.view;

import com.exammanager.controller.AppController;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

/**
 * The application's main view.
 * <p>
 * This class is responsible for the application's UI. It defines
 * the overall layout of the application.
 * <p>
 * The MainView class also initializes the AppController class once
 * the JavaFX scene graph is ready, connecting the various views
 * with the application logic.
 *
 * @author Bendik
 */
public class MainView extends VBox {

    private final TabPane tabPane = new TabPane();

    private final TeacherView teacherView = new TeacherView();
    private final StudentView studentView = new StudentView();
    private final ExamView examView = new ExamView();
    private final DepartmentView departmentView = new DepartmentView();

    public MainView() {
        // Creates the individual tabs
        Tab teachersTab = new Tab("Teachers", teacherView);

        Tab studentsTab = new Tab("Students", studentView);

        Tab examsTab = new Tab("Exams", examView);

        Tab departmentTab = new Tab("Departments", departmentView);

        // Adds tabs to tabPane
        tabPane.getTabs().addAll(teachersTab, studentsTab, examsTab);

        // TODO! ADD CONDITION
        tabPane.getTabs().add(departmentTab);

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

    public TabPane getTabPane() {
        return tabPane;
    }

    public DepartmentView getDepartmentView() {
        return departmentView;
    }

    public ExamView getExamView() {
        return examView;
    }
}