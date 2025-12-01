package com.exammanager.controller;

import com.exammanager.dao.DepartmentDAO;
import com.exammanager.dao.TeacherDAO;
import com.exammanager.login.AccessLevel;
import com.exammanager.model.Department;
import com.exammanager.util.AlertUtil;
import com.exammanager.dialog.TeacherDialog;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.exammanager.model.Teacher;
import com.exammanager.view.TeacherView;
import javafx.collections.transformation.FilteredList;

/**
 * Controller class for adding functionality to the TeacherView.
 * <p>
 * Handles interaction between {@link TeacherView} (UI) and
 * the {@link TeacherDAO} (data access layer). Provides methods
 * to load, add, edit and delete teachers, as well as to refresh
 * the teacher table in TeacherView.
 * <p>
 * @author Bendik
 */
public class TeacherController {

    // Reference to the teacher view
    private final TeacherView teacherView;

    // List of teachers from the database
    private ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

    // List of departments from the database
    private ObservableList<Department> departmentList = FXCollections.observableArrayList();

    // FilteredList wrapper for the teacherList, used to sort teachers
    private FilteredList<Teacher> filteredTeacherList = new FilteredList<>(teacherList, p -> true);

    // DAOs used for database operations
    private final TeacherDAO teacherDAO;
    private final DepartmentDAO departmentDAO;

    // Access level of the currently logged-in user
    private final AccessLevel accessLevel;

    public TeacherController(TeacherView view, TeacherDAO teacherDAO, DepartmentDAO departmentDAO, AccessLevel accessLevel) {
        this.teacherView = view;
        this.teacherDAO = teacherDAO;
        this.departmentDAO = departmentDAO;
        this.accessLevel = accessLevel;

        initialize();
    }

    private void initialize() {
        // Gets teachers from the database
        // Logs error to terminal if unsuccessful
        try {
            teacherList.setAll(teacherDAO.findAll());
        } catch(Exception e) {
            System.out.println("Failed to get teachers: " + e.getMessage());
        }

        // Adds teachers to the table in TeacherView using the filteredTeacherList to allow searching
        teacherView.getTeacherTable().setItems(filteredTeacherList);

        // Adds a listener to the search field and adds logic to allow searching all columns of the teacher table
        teacherView.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            String searchQuery = (newValue == null || newValue.isEmpty()) ? "" : newValue.trim().toLowerCase();
            filteredTeacherList.setPredicate(teacher ->
                    searchQuery.isEmpty() ||
                    Integer.toString(teacher.getId()).contains(searchQuery) ||
                    teacher.getFirstName().toLowerCase().contains(searchQuery) ||
                    teacher.getLastName().toLowerCase().contains(searchQuery) ||
                    teacher.getDepartment().toLowerCase().contains(searchQuery) ||
                    teacher.getEmail().toLowerCase().contains(searchQuery)
            );
        });

        updateComboBoxSelection();
        setUiElementAvailability();
        initButtonFunctionality();
        addTextFieldListeners();
        addTableListener();
    }

    // Set UI element visibility based on access the access level of the currently logged-in user
    private void setUiElementAvailability() {
        // Show CRUD controls for teachers only when logged in as an administrator
        if (accessLevel == AccessLevel.ADMIN) {
            teacherView.getControlBox().setVisible(true);
        }
    }

    // Set button functionality
    private void initButtonFunctionality() {

        // Adds functionality to the clear search button in TeacherView
        teacherView.getClearSearchButton().setOnMouseClicked(event -> {
            teacherView.getSearchField().clear();
        });

        // Adds functionality to the refresh button in TeacherView
        teacherView.getRefreshButton().setOnMouseClicked(event -> {
            refresh();
        });

        // Adds functionality to the edit button in TeacherView
        teacherView.getEditSelectedButton().setOnMouseClicked(event -> {
            Teacher selectedTeacher = teacherView.getTeacherTable().getSelectionModel().getSelectedItem();

            var result = TeacherDialog.editTeacherDialog(selectedTeacher, departmentList);

            if (result.isPresent()) {
                var resultTeacher = result.get();

                try {
                    teacherDAO.updateSingle(resultTeacher);
                    refresh();
                } catch(Exception e) {
                    AlertUtil.showDatabaseConnectionError("Error updating teacher. No database connection.");
                }
            }
        });

        // Adds functionality to the delete button in TeacherView
        teacherView.getDeleteSelectedButton().setOnMouseClicked(event -> {
            ObservableList<Teacher> selectedTeachers = teacherView.getTeacherTable().getSelectionModel().getSelectedItems();

            String alertTitleHeader = "Confirm deletion";
            String plural = selectedTeachers.size() == 1 ? "" : "s";
            String alertContent = "Are you sure you want to delete " + selectedTeachers.size() + " teacher" + plural + "?";

            if (AlertUtil.confirmationAlert(alertTitleHeader, alertTitleHeader, alertContent)) {
                try {
                    teacherDAO.deleteList(selectedTeachers);
                    refresh();
                } catch(Exception e) {
                    AlertUtil.showDatabaseConnectionError("Error deleting teacher" + plural + ". No database connection.");
                }
            }
        });

        // Adds functionality to the add teacher button in TeacherView
        teacherView.getAddButton().setOnMouseClicked(event -> {
            Teacher teacherToBeAdded = new Teacher(
                    teacherView.getFirstNameField().getText().trim(),
                    teacherView.getLastNameField().getText().trim(),
                    teacherView.getDepartmentComboBox().getValue().getName(),
                    teacherView.getEmailField().getText().trim()
            );

            try {
                teacherDAO.addSingle(teacherToBeAdded);
                teacherView.getFirstNameField().clear();
                teacherView.getLastNameField().clear();
                teacherView.getEmailField().clear();
                refresh();
            } catch (Exception e) {
                AlertUtil.showDatabaseConnectionError("Error adding teacher. No database connection.");
            }
        });
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

    private void refresh() {
        try {
            teacherList.setAll(teacherDAO.findAll());
            updateComboBoxSelection();
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while trying to refresh. No database connection.");
        }
    }

    // Update ComboBoxes with items from the database
    private void updateComboBoxSelection() {
        try {
            teacherView.getDepartmentComboBox().getSelectionModel().clearSelection();
            teacherView.getDepartmentComboBox().getItems().clear();
            departmentList.setAll(departmentDAO.findAll());
        } catch (Exception _) {}

        teacherView.getDepartmentComboBox().setItems(departmentList);
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
