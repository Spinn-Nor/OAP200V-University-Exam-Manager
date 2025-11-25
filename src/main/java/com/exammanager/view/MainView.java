package com.exammanager.view;

import com.exammanager.controller.AppController;
import com.exammanager.login.AccessLevel;
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
    private final CourseView courseView = new CourseView();
    private final ExamView examView = new ExamView();
    private final DepartmentView departmentView = new DepartmentView();

    public MainView(AccessLevel accessLevel) {

        // Creates the individual tabs
        Tab teachersTab = new Tab("Teachers", teacherView);

        Tab studentsTab = new Tab("Students", studentView);

        Tab courseTab = new Tab("Courses", courseView);

        Tab examsTab = new Tab("Exams", examView);

        Tab departmentTab = new Tab("Departments", departmentView);

        // Adds tabs to tabPane based on access level
        if (accessLevel == AccessLevel.ADMIN) {
            tabPane.getTabs().addAll(teachersTab, studentsTab, courseTab, examsTab, departmentTab);
        } else if (accessLevel == AccessLevel.TEACHER) {
            tabPane.getTabs().addAll(studentsTab, examsTab);
        } else {
            tabPane.getTabs().addAll(examsTab);
        }

        // Prevents closing tabs in the TabPane
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        getChildren().addAll(tabPane);

        Platform.runLater(() -> new AppController(this, accessLevel).start());
    }

    public TeacherView getTeacherView() {
        return teacherView;
    }

    public StudentView getStudentView() {
        return studentView;
    }

    public CourseView getCourseView() {
        return courseView;
    }

    public DepartmentView getDepartmentView() {
        return departmentView;
    }

    public ExamView getExamView() {
        return examView;
    }
}