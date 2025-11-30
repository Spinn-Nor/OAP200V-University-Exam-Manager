package com.exammanager.controller;

import com.exammanager.dao.*;
import com.exammanager.login.AccessLevel;
import com.exammanager.service.ExportService;
import com.exammanager.service.ManageUserService;
import com.exammanager.service.ReportService;
import com.exammanager.util.AlertUtil;
import com.exammanager.util.DatabaseConnection;
import com.exammanager.view.MainView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.sql.Connection;

public class AppController {

    private MainView mainView;
    private Connection conn;

    private TeacherDAO teacherDao;
    private StudentDAO studentDao;
    private CourseDAO courseDao;
    private ExamDAO examDao;
    private DepartmentDAO departmentDao;

    private ReportService reportService;
    private ExportService exportService;

    private AccessLevel accessLevel;

    /**
     * Controller for the application itself.
     * <p>
     * This controller-class is responsible for setting up the
     * shared database connection, passing the DB-connection to
     * the view-specific controllers, as well as connecting the
     * other controllers with their respective views.
     *
     * @param mainView the application's main view
     */
    public AppController(MainView mainView, AccessLevel accessLevel) {
        this.mainView = mainView;
        this.accessLevel = accessLevel;
    }

    public void start() {
        // create shared database connection
        conn = DatabaseConnection.getConnection();

        // setup DAOs if database connection succeeds
        if (conn != null) {
            teacherDao = new TeacherDAO(conn);
            studentDao = new StudentDAO(conn);
            courseDao = new CourseDAO(conn);
            examDao = new ExamDAO(conn);
            departmentDao = new DepartmentDAO(conn);
        }

        // initialize controllers for each view
        new TeacherController(mainView.getTeacherView(), teacherDao, departmentDao, accessLevel);
        new StudentController(mainView.getStudentView(), studentDao, accessLevel);
        new CourseController(mainView.getCourseView(), courseDao, teacherDao, accessLevel);
        new ExamController(mainView.getExamView(), examDao, studentDao, courseDao, accessLevel);
        new DepartmentController(mainView.getDepartmentView(), departmentDao, accessLevel);

        // initialize ReportService for generating reports
        reportService = new ReportService(mainView, courseDao, examDao, studentDao);

        // initialize ExportService for exporting the database
        exportService = new ExportService(mainView, teacherDao, studentDao, courseDao, examDao, departmentDao);

        setupMenuFunctionality();
    }

    private void setupMenuFunctionality() {
        mainView.getCheckDbItem().setOnAction(event -> {
            try (Connection test = DatabaseConnection.getConnection()) {
                if (test != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Connection Successful");
                    alert.setHeaderText("Successfully connected to the database.");
                    alert.showAndWait();
                } else {
                    AlertUtil.genericError("Connection failed", "Could not connect to the database.");
                }
            } catch (Exception e) {
                AlertUtil.showDatabaseConnectionError("Failed to connect to the database: " + e.getMessage());
            }
        });

        mainView.getExportItem().setOnAction(event -> exportService.exportDatabase());

        mainView.getExitItem().setOnAction(event -> Platform.exit());

        mainView.getCourseReportItem().setOnAction(event -> reportService.generateCourseReport());

        mainView.getStudentReportItem().setOnAction(event -> reportService.generateStudentReport(accessLevel.getEmail()));

        mainView.getAddUserItem().setOnAction(event -> ManageUserService.addUser());

        mainView.getDeleteUserItem().setOnAction(event -> ManageUserService.deleteUser(accessLevel.getEmail()));

        mainView.getAboutItem().setOnAction(event -> AlertUtil.showAppInformation());
    }

}
