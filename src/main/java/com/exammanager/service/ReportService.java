package com.exammanager.service;

import com.exammanager.dao.CourseDAO;
import com.exammanager.dao.ExamDAO;
import com.exammanager.dao.StudentDAO;
import com.exammanager.model.Course;
import com.exammanager.model.Exam;
import com.exammanager.model.Student;
import com.exammanager.util.AlertUtil;
import com.exammanager.view.MainView;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

public class ReportService {

    private final MainView mainView;

    private final CourseDAO courseDAO;
    private final ExamDAO examDAO;
    private final StudentDAO studentDAO;

    public ReportService(MainView mainView, CourseDAO courseDAO, ExamDAO examDAO, StudentDAO studentDAO) {
        this.mainView = mainView;
        this.courseDAO = courseDAO;
        this.examDAO = examDAO;
        this.studentDAO = studentDAO;
    }

    public void generateCourseReport() {
        if (courseDAO == null || examDAO == null) {
            AlertUtil.showDatabaseConnectionError("Cannot generate reports without an active database connection");
            return;
        }

        Optional<Course> dialogResult = selectCourseDialog(courseDAO);
        if (dialogResult.isEmpty()) {
            return;
        }

        Window owner = mainView.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select directory");
        File dir = directoryChooser.showDialog(owner);
        if (dir == null) {
            return;
        }

        ObservableList<Exam> exams = examDAO.findByCourseId(dialogResult.get().getId());

        String fileName = dialogResult.get().getTitle() + "_report.txt";
        File courseReport = new File(dir, fileName);
        try (BufferedWriter w = Files.newBufferedWriter(courseReport.toPath(), StandardCharsets.UTF_8)) {
            w.write("Course report");
            w.newLine();
            w.write("Course ID: " +  dialogResult.get().getId());
            w.write("Course Title: " + dialogResult.get().getTitle());
            w.newLine();
            for (Exam exam : exams) {
                w.write("----------------");
                w.newLine();
                w.write("Exam ID: " + exam.getId());
                w.newLine();
                w.write("Student ID: " + exam.getStudentId());
                w.newLine();
                w.write("Date: " + exam.getExamDate());
                w.newLine();
                w.write("Grade: " + exam.getGrade());
                w.newLine();
            }
            reportFinishedDialog();
        } catch (Exception e) {
            AlertUtil.genericError("Error", "Generating report failed. " + e.getMessage());
        }
    }

    public void generateStudentReport(String email) {
        if (examDAO == null || studentDAO == null) {
            AlertUtil.showDatabaseConnectionError("Cannot generate reports without an active database connection");
            return;
        }

        Window owner = mainView.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select directory");
        File dir = directoryChooser.showDialog(owner);
        if (dir == null) {
            return;
        }

        Optional<Student> studentResult = studentDAO.findByEmail(email);
        if (studentResult.isEmpty()) {
            AlertUtil.genericError("Error", "Could not generate report: \nFailed to get student.");
            return;
        }

        ObservableList<Exam> exams = examDAO.findAllByEmail(email);

        String fileName = "report_card.txt";
        File reportCard = new File(dir, fileName);
        try (BufferedWriter w = Files.newBufferedWriter(reportCard.toPath(), StandardCharsets.UTF_8)) {
            w.write("Report Card");
            w.newLine();
            w.write("Student ID:" + studentResult.get().getId());
            w.newLine();
            w.write("Name: " + studentResult.get().getFirstName() + " " + studentResult.get().getLastName());
            w.newLine();
            for (Exam exam : exams) {
                w.write("----------------");
                w.newLine();
                w.write("Exam ID: " + exam.getId());
                w.newLine();
                w.write("Student ID: " + exam.getStudentId());
                w.newLine();
                w.write("Date: " + exam.getExamDate());
                w.newLine();
                w.write("Grade: " + exam.getGrade());
                w.newLine();
            }
        } catch (Exception e) {
            AlertUtil.genericError("Error", "Generating report failed. " + e.getMessage());
        }
    }

    private Optional<Course> selectCourseDialog(CourseDAO courseDAO) {
        Dialog<Course> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setHeight(200);
        dialog.setWidth(300);
        dialog.setTitle("Select Course");
        dialog.setHeaderText("Which course do you want to generate a report for?");
        dialog.resizableProperty().setValue(false);

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);

        Label courseLabel = new Label("Course:");
        content.getChildren().add(courseLabel);

        ObservableList<Course> courses = courseDAO.findAll();
        ComboBox<Course> courseComboBox = new ComboBox<>(courses);
        content.getChildren().add(courseComboBox);

        ButtonType closeButton = new ButtonType("Close", ButtonType.CLOSE.getButtonData());
        ButtonType saveButton = new ButtonType("Select", ButtonType.APPLY.getButtonData());

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(closeButton, saveButton);
        dialog.getDialogPane().lookupButton(saveButton).disableProperty().setValue(true);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                return courseComboBox.getValue();
            } else  {
                return null;
            }
        });

        courseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dialog.getDialogPane().lookupButton(saveButton).disableProperty().setValue(false);
            }
        });

        return dialog.showAndWait();
    }

    private void reportFinishedDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Report Finished");
        dialog.setContentText("Finished generating report.");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();
    }

}
