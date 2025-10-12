package com.exammanager.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.exammanager.model.Teacher;
import com.exammanager.view.TeacherView;

public class TeacherController {

    // Reference to the teacher view
    private final TeacherView teacherView;

    // List of teachers used to populate teacherTable in TeacherView
    private final ObservableList<Teacher> teacherList;

    public TeacherController(TeacherView view) {
        this.teacherView = view;
        teacherList = FXCollections.observableArrayList();

        initialize();
    }

    private void initialize() {
        // TODO! REMOVE AFTER TESTING
        // Adds example teachers to the table for testing
        var teacherList = Teacher.generateExampleTeachers();
        System.out.println("Teacher List: " + teacherList);
        teacherView.getTeacherTable().setItems(teacherList);

        addTextFieldListeners();
        addTableListener();
    }

    // Adds ChangeListeners to TextFields in TeacherView
    private void addTextFieldListeners() {
        // TODO! ADD DEPARTMENT 
        var firstNameField = teacherView.getFirstNameField();
        var lastNameField = teacherView.getLastNameField();
        // var departmentList ...
        var emailField = teacherView.getEmailField();
        var addButton = teacherView.getAddButton();

        // Defines a lambda expression to change button state depending on content of text fields
        // Button set to disabled if either field is empty
        Runnable updateButtonState = () -> {
            boolean disableButton =
            firstNameField.getText().trim().isEmpty() ||
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty();

            addButton.setDisable(disableButton);
        };

        // Defines a listener that runs updateButtonState when the text in firstNameField, lastNameField or emailField in teacherView changes
        ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> updateButtonState.run();

        // Adds the listener to firstNameField, lastNameField & emailField in teacherView
        firstNameField.textProperty().addListener(textFieldListener);
        lastNameField.textProperty().addListener(textFieldListener);
        emailField.textProperty().addListener(textFieldListener);
    }

    // Adds a ChangeListener to the table in teacherView
    private void addTableListener() {
        var table = teacherView.getTeacherTable();
        var editButton = teacherView.getEditSelectedButton();
        var deleteButton = teacherView.getDeleteSelectedButton();

        var tableSelectionModel = table.getSelectionModel();

        // Adds a listener to teacherTable that runs code whenever a row is selected/deselected
        tableSelectionModel.selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            var selectedCount = tableSelectionModel.getSelectedItems().size();

            // Enables the edit button only when exactly 1 row is selected
            editButton.setDisable(selectedCount != 1);

            // Enables the delete button if at least one row is selected
            deleteButton.setDisable(selectedCount == 0);
        });
    }
}
