package com.exammanager.controller;

import com.exammanager.dao.*;
import com.exammanager.login.AccessLevel;
import com.exammanager.model.*;
import com.exammanager.service.ReportService;
import com.exammanager.util.AlertUtil;
import com.exammanager.util.DatabaseConnection;
import com.exammanager.view.MainView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.List;

public class AppController {

    private MainView mainView;
    private Connection conn;

    private TeacherDAO teacherDao;
    private StudentDAO studentDao;
    private CourseDAO courseDao;
    private ExamDAO examDao;
    private DepartmentDAO departmentDao;

    private ReportService reportService;

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

        mainView.getExportItem().setOnAction(event -> exportDatabase());

        mainView.getExitItem().setOnAction(event -> Platform.exit());

        mainView.getCourseReportItem().setOnAction(event -> reportService.generateCourseReport());

        mainView.getStudentReportItem().setOnAction(event -> reportService.generateStudentReport(accessLevel.getEmail()));

        mainView.getAboutItem().setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("About Exam Manager");
            alert.setContentText("Exam Manager is a simple JavaFX application. \n" +
                    "This application is used for managing teachers, students and exams at a university.");
            alert.showAndWait();
        });
    }

    private void exportDatabase() {
        if (teacherDao == null || studentDao == null || courseDao == null || examDao == null || departmentDao == null) {
            AlertUtil.showDatabaseConnectionError("Cannot export database without an active database connection");
            return;
        }

        Window owner = mainView.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Export Directory");
        File dir = directoryChooser.showDialog(owner);
        if (dir == null) {
            return;
        }

        try {
            // Export Departments
            List<Department> departments = departmentDao.findAll();
            File departmentsCsv = new File(dir, "departments.csv");
            try (BufferedWriter w = Files.newBufferedWriter(departmentsCsv.toPath(), StandardCharsets.UTF_8)) {
                w.write("id, name");
                w.newLine();
                for (Department department : departments) {
                    w.write(department.getId() + "," +  department.getName());
                    w.newLine();
                }
            }

            // Export Teachers
            List<Teacher> teachers = teacherDao.findAll();
            File teacherCsv = new File(dir, "teachers.csv");
            try (BufferedWriter w = Files.newBufferedWriter(teacherCsv.toPath(), StandardCharsets.UTF_8)) {
                w.write("id, first_name, last_name, department, email");
                w.newLine();
                for (Teacher teacher : teachers) {
                    w.write(teacher.getId() + "," + teacher.getFirstName() + "," + teacher.getLastName() + "," + teacher.getDepartment() + "," + teacher.getEmail());
                    w.newLine();
                }
            }

            // Export Students
            List<Student> students = studentDao.findAll();
            File studentCsv = new File(dir, "students.csv");
            try (BufferedWriter w = Files.newBufferedWriter(studentCsv.toPath(), StandardCharsets.UTF_8)) {
                w.write("id, first_name, last_name, email, enrollment_year");
                w.newLine();
                for (Student student : students) {
                    w.write(student.getId() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getEmail() + "," + student.getEnrollmentYear());
                    w.newLine();
                }
            }

            // Export Courses
            List<Course> courses = courseDao.findAll();
            File coursesCsv = new File(dir, "courses.csv");
            try (BufferedWriter w = Files.newBufferedWriter(coursesCsv.toPath(), StandardCharsets.UTF_8)) {
                w.write("id, course_code, title, credits, teacher_id");
                w.newLine();
                for (Course course : courses) {
                    w.write(course.getId() + "," + course.getCourseCode() + "," + course.getTitle() + "," + course.getCredits() + "," + course.getTeacherId());
                    w.newLine();
                }
            }

            // Export Exams
            List<Exam> exams = examDao.findAll();
            File examCsv = new File(dir, "exams.csv");
            try (BufferedWriter w = Files.newBufferedWriter(examCsv.toPath(), StandardCharsets.UTF_8)) {
                w.write("id, student_id, course_id, exam_date, grade");
                w.newLine();
                for (Exam exam : exams) {
                    w.write(exam.getId() + "," + exam.getStudentId() + "," + exam.getCourseId() + "," + exam.getExamDate() + "," + exam.getGrade());
                    w.newLine();
                }
            }

        } catch (Exception e) {
            AlertUtil.genericError("Export failed", "Failed to export the database.\n" + e.getMessage());
        }

    }

}
