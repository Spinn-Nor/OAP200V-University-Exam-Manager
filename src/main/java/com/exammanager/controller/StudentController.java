package com.exammanager.controller;

import com.exammanager.model.Student;
import com.exammanager.view.StudentView;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Victoria
public class StudentController {

    // Reference to the student view
    private final StudentView studentView;

    // List of students used to populate studentTable in StudentView
    private final ObservableList<Student> studentList;

    public StudentController(StudentView view) {
        this.studentView = view;
        studentList = FXCollections.observableArrayList();

        initialize();
    }

    //Victoria
    private void initialize() {
        // TODO! REMOVE AFTER TESTING
        // Adds example students to the table for testing
        var studentList = Student.generateExampleStudents();
        System.out.println("Student List: " + studentList);
        studentView.getStudentTable().setItems(studentList);

        addTextFieldListeners();
        addTableListener();
    }

    //Victoria
    // Adds ChangeListeners to TextFields in StudentView
    private void addTextFieldListeners() {
        // TODO! ADD DEPARTMENT
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

        // Adds the listener to firstNameField, lastNameField, emailField & enrollmentYearField in studentView
        firstNameField.textProperty().addListener(textFieldListener);
        lastNameField.textProperty().addListener(textFieldListener);
        emailField.textProperty().addListener(textFieldListener);
        enrollmentYearField.textProperty().addListener(textFieldListener);
    }

    //Victoria
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

