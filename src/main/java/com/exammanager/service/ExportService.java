package com.exammanager.service;

import com.exammanager.dao.*;
import com.exammanager.model.*;
import com.exammanager.util.AlertUtil;
import com.exammanager.view.MainView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class ExportService {

    private final MainView mainView;

    private final TeacherDAO teacherDao;
    private final StudentDAO studentDao;
    private final CourseDAO courseDao;
    private final ExamDAO examDao;
    private final DepartmentDAO departmentDao;

    public ExportService(MainView mainView, TeacherDAO teacherDAO, StudentDAO studentDAO, CourseDAO courseDAO, ExamDAO examDAO, DepartmentDAO departmentDAO) {
        this.mainView = mainView;
        this.teacherDao = teacherDAO;
        this.studentDao = studentDAO;
        this.courseDao = courseDAO;
        this.examDao = examDAO;
        this.departmentDao = departmentDAO;
    }

    public void exportDatabase() {
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
            File departmentsCsv = new File(dir, "department.csv");
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
            File teacherCsv = new File(dir, "teacher.csv");
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
            File studentCsv = new File(dir, "student.csv");
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
            File coursesCsv = new File(dir, "course.csv");
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
            File examCsv = new File(dir, "exam.csv");
            try (BufferedWriter w = Files.newBufferedWriter(examCsv.toPath(), StandardCharsets.UTF_8)) {
                w.write("id, student_id, course_id, exam_date, grade");
                w.newLine();
                for (Exam exam : exams) {
                    w.write(exam.getId() + "," + exam.getStudentId() + "," + exam.getCourseId() + "," + exam.getExamDate() + "," + exam.getGrade());
                    w.newLine();
                }
            }

            AlertUtil.showInformation("Export finished", "Finished exporting the database.");
        } catch (Exception e) {
            AlertUtil.genericError("Export failed", "Failed to export the database.\n" + e.getMessage());
        }

    }

}
