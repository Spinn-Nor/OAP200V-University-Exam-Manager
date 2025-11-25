package com.exammanager.controller;

import com.exammanager.dao.StudentDAO;
import com.exammanager.dialog.StudentDialog;
import com.exammanager.login.AccessLevel;
import com.exammanager.model.Student;
import com.exammanager.util.AlertUtil;
import com.exammanager.view.StudentView;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Controller class for adding functionality to the StudentView.
 * <p>
 * Handles interaction between {@link StudentView} (UI) and
 * the {@link StudentDAO} (data access layer). Provides methods
 * to load, add, edit and delete students, as well as to refresh
 * the student table from StudentView.
 * <p>
 * @author Victoria & Bendik
 */
public class StudentController {

    // Reference to the student view
    private final StudentView studentView;

    // List of students from the database
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    // FilteredList wrapper for the studentList, used to sort students
    private FilteredList<Student> filteredStudentList = new FilteredList<>(studentList, p -> true);

    // DAO used for database operations on students
    private final StudentDAO studentDAO;

    private final AccessLevel accessLevel;

    public StudentController(StudentView view, StudentDAO studentDAO, AccessLevel accessLevel) {
        this.studentView = view;
        this.studentDAO = studentDAO;
        this.accessLevel = accessLevel;

        initialize();
    }

    // Victoria & Bendik
    private void initialize() {
        // Gets students from the database
        // FIXME! GENERATES EXAMPLE STUDENTS IF NO DATABASE CONNECTION
        try {
            studentList.setAll(studentDAO.findAll());
        } catch (Exception e) {
            studentList.setAll(Student.generateExampleStudents());
        }

        // Adds students to the table in StudentView using the filteredTeacherList to allow searching
        studentView.getStudentTable().setItems(filteredStudentList);

        // Adds a listener to the search field and adds logic to allow searching all columns of the teacher table
        studentView.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            String searchQuery = (newValue == null || newValue.isEmpty()) ? "" : newValue.trim().toLowerCase();
            filteredStudentList.setPredicate(student ->
                    searchQuery.isEmpty() ||
                    Integer.toString(student.getId()).contains(searchQuery) ||
                    student.getFirstName().toLowerCase().contains(searchQuery) ||
                    student.getLastName().toLowerCase().contains(searchQuery) ||
                    student.getEmail().toLowerCase().contains(searchQuery) ||
                    Integer.toString(student.getEnrollmentYear()).contains(searchQuery));
        });

        setUiElementAvailability();
        initButtonFunctionality();
        addTextFieldListeners();
        addTableListener();
    }

    // Bendik
    // Set UI element visibility based on access the access level of the currently logged-in user
    private void setUiElementAvailability() {
        // accessLevel = AccessLevel.STUDENT;

        if (accessLevel == AccessLevel.ADMIN) {
            studentView.getControlBox().setVisible(true);
        }
    }

    // Bendik
    // Set button functionality
    private void initButtonFunctionality() {

        // Adds functionality to the clear search button in StudentView
        studentView.getClearSearchButton().setOnMouseClicked(event -> {
            studentView.getSearchField().clear();
        });

        // Adds functionality to the refresh button in StudentView
        studentView.getRefreshButton().setOnMouseClicked(event -> {
            refreshStudentTable();
        });

        // Adds functionality to the edit button in StudentView
        studentView.getEditSelectedButton().setOnMouseClicked(event -> {
            Student selectedStudent = studentView.getStudentTable().getSelectionModel().getSelectedItem();

            var result = StudentDialog.editStudentDialog(selectedStudent);

            if (result.isPresent()) {
                var resultStudent = result.get();

                try {
                    studentDAO.updateSingle(resultStudent);
                    refreshStudentTable();
                } catch (Exception e) {
                    AlertUtil.showDatabaseConnectionError("Error updating student. No database connection.");
                }
            }
        });

        // Adds functionality to the delete button in StudentView
        studentView.getDeleteSelectedButton().setOnMouseClicked(event -> {
            ObservableList<Student> selectedStudents = studentView.getStudentTable().getSelectionModel().getSelectedItems();

            String alertTitleHeader = "Confirm deletion";
            String plural = selectedStudents.size() == 1 ? "" : "s";
            String alertContent = "Are you sure you want to delete " + selectedStudents.size() + " student" + plural + "?";

            if (AlertUtil.confirmationAlert(alertTitleHeader, alertTitleHeader, alertContent)) {
                try {
                    studentDAO.deleteList(selectedStudents);
                    refreshStudentTable();
                } catch (Exception e) {
                    AlertUtil.showDatabaseConnectionError("Error deleting student(s). No database connection.");
                }
            }
        });

        // Adds functionality to the add student button in StudentView
        studentView.getAddButton().setOnMouseClicked(event -> {
            int newStudentEnrollmentYear = Integer.parseInt(studentView.getEnrollmentYearField().getText().trim());
            Student studentToBeAdded = new Student(
                    studentView.getFirstNameField().getText().trim(),
                    studentView.getLastNameField().getText().trim(),
                    studentView.getEmailField().getText().trim(),
                    newStudentEnrollmentYear
            );

            try {
                studentDAO.addSingle(studentToBeAdded);
                studentView.getFirstNameField().clear();
                studentView.getLastNameField().clear();
                studentView.getEmailField().clear();
                studentView.getEnrollmentYearField().clear();
                refreshStudentTable();
            } catch (Exception e) {
                AlertUtil.showDatabaseConnectionError("Error adding student. No database connection.");
            }
        });
    }

    // Bendik
    private void refreshStudentTable() {
        try {
            studentList.setAll(studentDAO.findAll());
        }  catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while trying to refresh. No database connection.");
        }
    }

    // Victoria & Bendik
    // Adds ChangeListeners to TextFields in StudentView
    private void addTextFieldListeners() {
        var firstNameField = studentView.getFirstNameField();
        var lastNameField = studentView.getLastNameField();
        var emailField = studentView.getEmailField();
        var enrollmentYearField = studentView.getEnrollmentYearField();
        var addButton = studentView.getAddButton();

        // Defines a lambda expression to change button state depending on content of text fields
        // Button set to disabled if either field is empty
        Runnable updateButtonState = () -> {
            boolean disableButton =
                firstNameField.getText().trim().isEmpty() ||
                lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                enrollmentYearField.getText().trim().isEmpty();

            addButton.setDisable(disableButton);
        };

        // Defines a listener that runs updateButtonState when the text in firstNameField, lastNameField, emailField or enrollmentYear in studentView changes
        ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> updateButtonState.run();

        // Listener that prevents anything but digits from being entered into the enrollment year text field
        ChangeListener<String> enrollmentYearNonDigitListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                enrollmentYearField.setText(oldValue);
            }
        };

        // Adds the listener to firstNameField, lastNameField, emailField & enrollmentYearField in studentView
        firstNameField.textProperty().addListener(textFieldListener);
        lastNameField.textProperty().addListener(textFieldListener);
        emailField.textProperty().addListener(textFieldListener);
        enrollmentYearField.textProperty().addListener(textFieldListener);
        enrollmentYearField.textProperty().addListener(enrollmentYearNonDigitListener);
    }

    // Victoria
    // Adds a ChangeListener to the table in studentView
    private void addTableListener() {
        var table = studentView.getStudentTable();
        var editButton = studentView.getEditSelectedButton();
        var deleteButton = studentView.getDeleteSelectedButton();

        var tableSelectionModel = table.getSelectionModel();

        // Adds a listener to studentTable that runs code whenever a row is selected/deselected
        tableSelectionModel.selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            var selectedCount = tableSelectionModel.getSelectedItems().size();

            // Enables the edit button only when exactly 1 row is selected
            editButton.setDisable(selectedCount != 1);

            // Enables the delete button if at least one row is selected
            deleteButton.setDisable(selectedCount == 0);
        });
    }
}

