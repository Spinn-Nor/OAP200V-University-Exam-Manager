package com.exammanager.controller;

import com.exammanager.dao.*;
import com.exammanager.login.AccessLevel;
import com.exammanager.util.DatabaseConnection;
import com.exammanager.view.MainView;
import com.exammanager.controller.TeacherController;

import java.sql.Connection;

public class AppController {

    private MainView mainView;
    private Connection conn;

    private TeacherDAO teacherDao;
    private StudentDAO studentDao;
    private CourseDAO courseDao;
    private ExamDAO examDao;
    private DepartmentDAO departmentDao;

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
        new ExamController(mainView.getExamView(), examDao, studentDao, courseDao, accessLevel);
        new DepartmentController(mainView.getDepartmentView(), departmentDao);
    }
}
