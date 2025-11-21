package com.exammanager.controller;

import com.exammanager.dao.StudentDAO;
import com.exammanager.dao.TeacherDAO;
import com.exammanager.util.DatabaseConnection;
import com.exammanager.view.MainView;
import com.exammanager.controller.TeacherController;

import java.sql.Connection;

public class AppController {

    private MainView mainView;
    private Connection conn;

    private TeacherDAO teacherDao;
    private StudentDAO studentDao;

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
    public AppController(MainView mainView) {
        this.mainView = mainView;
    }

    public void start() {
        // create shared database connection
        conn = DatabaseConnection.getConnection();

        // setup DAOs if database connection succeeds
        if (conn != null) {
            teacherDao = new TeacherDAO(conn);
            studentDao = new StudentDAO(conn);
        }

        // initialize controllers for each view
        new TeacherController(mainView.getTeacherView(), teacherDao);
        new StudentController(mainView.getStudentView(), studentDao);
    }
}
