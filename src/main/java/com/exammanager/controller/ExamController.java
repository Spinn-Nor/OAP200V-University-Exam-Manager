package com.exammanager.controller;

import com.exammanager.dao.CourseDAO;
import com.exammanager.dao.StudentDAO;
import com.exammanager.dialog.ExamDialog;
import com.exammanager.login.AccessLevel;
import com.exammanager.model.Course;
import com.exammanager.model.Exam;
import com.exammanager.model.Student;
import com.exammanager.util.AlertUtil;
import com.exammanager.view.ExamView;
import com.exammanager.dao.ExamDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.time.LocalDate;

/**
 * Controller class for adding functionality to the ExamView.
 * <p>
 * Handles interaction between {@link ExamView} (UI) and
 * the {@link ExamDAO} (data access layer). Provides methods
 * to load, add, edit and delete exams, as well as to refresh
 * the exam table from ExamView.
 * <p>
 * @author Bendik
 */
public class ExamController {

    // Reference to the exam view
    private final ExamView examView;

    // List of exams from the database
    private ObservableList<Exam> examList = FXCollections.observableArrayList();

    // List of students from the database
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    // List of courses form the database
    private ObservableList<Course> courseList = FXCollections.observableArrayList();

    // FilteredList wrapper for the examList, used to sort exams
    private FilteredList<Exam> filteredExamList = new FilteredList<Exam>(examList, p -> true);

    // DAOs used for database operations
    private final ExamDAO examDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    // Access level of the currently logged-in user
    private AccessLevel accessLevel;

    public ExamController(ExamView examView, ExamDAO examDAO, StudentDAO studentDAO, CourseDAO courseDAO, AccessLevel accessLevel) {
        this.examView = examView;
        this.examDAO = examDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.accessLevel = accessLevel;

        initialize();
    }

    private void initialize() {
        // Gets exams from the database
        try {
            examList.setAll(examDAO.findAll());
        } catch (Exception e) {
            // FIXME! Handle error
            Exam testExam = new Exam(1, 1, 1, LocalDate.now(), "A");
            examList.add(testExam);
        }

        // Adds exams to the table in ExamView using the filteredExamList to allow searching
        examView.getExamTable().setItems(filteredExamList);

        // Adds a listener to the search field and adds logic to allow searching all columns of the teacher table
        examView.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            String searchQuery = (newValue == null || newValue.isEmpty()) ? "" : newValue.trim().toLowerCase();
            filteredExamList.setPredicate(exam ->
                    searchQuery.isEmpty() ||
                            Integer.toString(exam.getId()).contains(searchQuery) ||
                            Integer.toString(exam.getCourseId()).contains(searchQuery) ||
                            Integer.toString(exam.getStudentId()).contains(searchQuery)
            );
        });

        updateComboBoxSelection();
        setUiElementAvailability();
        initButtonFunctionality();
        addComboBoxListeners();
        addTableListener();
    }

    private void setUiElementAvailability() {}

    private void initButtonFunctionality() {
        // Adds functionality to the clear search button in ExamView
        examView.getClearSearchButton().setOnMouseClicked(event -> {
            examView.getSearchField().clear();
        });

        // Adds functionality to the refresh button in ExamView
        examView.getRefreshButton().setOnMouseClicked(event -> {
            refreshExamView();
        });

        // Adds functionality to the edit button in ExamView
        examView.getEditSelectedButton().setOnMouseClicked(event -> {
            Exam selectedExam = examView.getExamTable().getSelectionModel().getSelectedItem();

            var result = ExamDialog.editExamDialog(selectedExam);

            if (result.isPresent()) {
                var resultExam = result.get();

                try {
                    examDAO.updateSingle(resultExam);
                    refreshExamView();
                } catch (Exception e) {
                    AlertUtil.showDatabaseConnectionError("Error updating exam. No database connection.");
                }
            }
        });

        // Adds functionality to the delete button in ExamView
        examView.getDeleteSelectedButton().setOnMouseClicked(event -> {
            ObservableList<Exam> selectedExam = examView.getExamTable().getSelectionModel().getSelectedItems();

            String alertTitleHeader = "Confirm deletion";
            String plural = selectedExam.size() == 1 ? "" : "s";
            String alertContent = "Are you sure you want to delete " + selectedExam.size() + " exam" + plural + "?";

            if (AlertUtil.confirmationAlert(alertTitleHeader, alertTitleHeader, alertContent)) {
                try {
                    examDAO.deleteList(selectedExam);
                    refreshExamView();
                } catch(Exception e) {
                    AlertUtil.showDatabaseConnectionError("Error deleting exam(s). No database connection.");
                }
            }
        });

        // Adds functionality to the add teacher button in TeacherView
        examView.getAddButton().setOnMouseClicked(event -> {
            Exam examToBeAdded = new Exam(
                    examView.getStudentIdComboBox().getValue().getId(),
                    examView.getCourseIdComboBox().getValue().getId(),
                    examView.getExamDatePicker().getValue(),
                    examView.getGradeComboBox().getValue()
            );

            try {
                examDAO.addSingle(examToBeAdded);
                examView.getStudentIdComboBox().getSelectionModel().clearSelection();
                examView.getCourseIdComboBox().getSelectionModel().clearSelection();
                refreshExamView();
            } catch (Exception e) {
                AlertUtil.showDatabaseConnectionError("Error adding exam. No database connection.");
                examView.getStudentIdComboBox().getSelectionModel().clearSelection();
                examView.getCourseIdComboBox().getSelectionModel().clearSelection();
            }
        });
    }

    private void addComboBoxListeners() {
        var addButton = examView.getAddButton();
        var studentIdComboBox = examView.getStudentIdComboBox();
        var courseIdComboBox = examView.getCourseIdComboBox();

        Runnable updateButtonState = () -> {
            boolean disableButton =
                    studentIdComboBox.getSelectionModel().isEmpty() ||
                    courseIdComboBox.getSelectionModel().isEmpty();

            addButton.setDisable(disableButton);
        };

        studentIdComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateButtonState.run();
        });

        courseIdComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateButtonState.run();
        });
    }

    private void refreshExamView() {
        try {
            examList.setAll(examDAO.findAll());
            updateComboBoxSelection();
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while trying to refresh. No database connection.");
        }
    }

    // Update ComboBoxes with items from the database
    private void updateComboBoxSelection() {
        try {
            examView.getStudentIdComboBox().getSelectionModel().clearSelection();
            examView.getCourseIdComboBox().getSelectionModel().clearSelection();

            examView.getStudentIdComboBox().getItems().clear();
            examView.getCourseIdComboBox().getItems().clear();

            studentList.setAll(studentDAO.findAll());
            courseList.setAll(courseDAO.findAll());
        } catch (Exception e) {
            studentList.setAll(Student.generateExampleStudents());
            courseList.setAll(Course.generateExampleCourses());
        }

        for (Student student : studentList) {
            examView.getStudentIdComboBox().getItems().add(student);
        }

        for (Course course : courseList) {
            examView.getCourseIdComboBox().getItems().add(course);
        }
    }

    private void addTableListener() {
        var table = examView.getExamTable();
        var editButton = examView.getEditSelectedButton();
        var deleteButton = examView.getDeleteSelectedButton();

        var tableSelectionModel = table.getSelectionModel();

        // Adds a listener to examTable that runs code whenever a row is selected/deselected
        tableSelectionModel.selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            var selectedCount = tableSelectionModel.getSelectedItems().size();

            // Enables the edit button only when exactly 1 row is selected
            editButton.setDisable(selectedCount != 1);

            // Enables the delete button if at least one row is selected
            deleteButton.setDisable(selectedCount == 0);
        });
    }

}
