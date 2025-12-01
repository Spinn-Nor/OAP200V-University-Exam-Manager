package com.exammanager.view;

import com.exammanager.controller.AppController;
import com.exammanager.login.AccessLevel;
import javafx.application.Platform;
import javafx.scene.control.*;
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


    private final TeacherView teacherView = new TeacherView();
    private final StudentView studentView = new StudentView();
    private final CourseView courseView = new CourseView();
    private final ExamView examView = new ExamView();
    private final DepartmentView departmentView = new DepartmentView();

    // Menu bar and menus
    private final MenuBar menuBar = new MenuBar();
    private final Menu fileMenu = new Menu("File");
    private final Menu reportMenu  = new Menu("Report");
    private final Menu userMenu = new Menu("User");
    private final Menu helpMenu = new Menu("Help");

    // Menu options under File
    private final MenuItem checkDbItem = new MenuItem("Check Database Connection");
    private final MenuItem exportItem = new MenuItem("Export Database");
    private final MenuItem exitItem = new MenuItem("Exit");

    // Menu options under Report
    private final MenuItem courseReportItem  = new MenuItem("Generate Course Report");
    private final MenuItem studentReportItem = new MenuItem("Generate Report Card");

    // Menu options under User
    private final MenuItem addUserItem = new MenuItem("Add User");
    private final MenuItem deleteUserItem = new MenuItem("Delete User");

    // Menu options under Help
    private final MenuItem aboutItem = new MenuItem("About");

    public MainView(AccessLevel accessLevel) {
        // Set up menu bar
        menuBar.setUseSystemMenuBar(true);

        // Add menu options
        fileMenu.getItems().add(checkDbItem);

        // Add exportItem to file menu if logged in as an administrator
        if (accessLevel == AccessLevel.ADMIN) {
            fileMenu.getItems().add(exportItem);
        }

        fileMenu.getItems().add(exitItem);

        // Add courseReportItem to report menu if logged in as an administrator or teacher
        // Add studentReportItem to report menu if logged in as a student
        if (accessLevel != AccessLevel.STUDENT) {
            reportMenu.getItems().add(courseReportItem);
        } else {
            reportMenu.getItems().add(studentReportItem);
        }

        userMenu.getItems().addAll(addUserItem, deleteUserItem);

        helpMenu.getItems().addAll(aboutItem);

        // Add menus to menu bar
        menuBar.getMenus().addAll(fileMenu, reportMenu);

        // Add User menu to menu bar if logged in as an administrator
        menuBar.getMenus().add(userMenu);

        menuBar.getMenus().add(helpMenu);

        // TabPane for selecting the individual views
        final TabPane tabPane = new TabPane();

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
            tabPane.getTabs().addAll(studentsTab, courseTab, examsTab);
        } else {
            tabPane.getTabs().addAll(examsTab);
        }

        // Prevents closing tabs in the TabPane
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        getChildren().addAll(menuBar, tabPane);

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

    public MenuItem getCheckDbItem() {
        return checkDbItem;
    }

    public MenuItem getExportItem() {
        return exportItem;
    }

    public MenuItem getExitItem() {
        return exitItem;
    }

    public MenuItem getCourseReportItem() {
        return courseReportItem;
    }

    public MenuItem getStudentReportItem() {
        return studentReportItem;
    }

    public MenuItem getAddUserItem() {
        return addUserItem;
    }

    public MenuItem getDeleteUserItem() {
        return deleteUserItem;
    }

    public MenuItem getAboutItem() {
        return aboutItem;
    }
}