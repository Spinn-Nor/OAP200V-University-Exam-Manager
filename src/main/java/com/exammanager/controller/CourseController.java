package com.exammanager.controller;

import com.exammanager.dao.CourseDAO;
import com.exammanager.dao.TeacherDAO;
import com.exammanager.dialog.CourseDialog;
import com.exammanager.login.AccessLevel;
import com.exammanager.model.Course;
import com.exammanager.model.Teacher;
import com.exammanager.util.AlertUtil;
import com.exammanager.view.CourseView;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Controller class for adding functionality to the CourseView.
 * <p>
 * Handles interaction between {@link CourseView} (UI) and
 * the {@link CourseDAO} (data access layer). Provides methods
 * to load, add, edit and delete courses, as well as to refresh
 * the course table in CourseView.
 * <p>
 * @author Bendik
 */
public class CourseController {

    // Reference to the course view
    private final CourseView courseView;

    // List of courses from the database
    private ObservableList<Course> courseList = FXCollections.observableArrayList();

    // List of teachers from the database
    private ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

    // FilteredList wrapper for the courseList, used to sort courses
    private FilteredList<Course> filteredCourseList = new FilteredList<>(courseList, p -> true);

    // DAOs used for database operations
    private final CourseDAO courseDAO;
    private final TeacherDAO teacherDAO;

    // Access level of the currently logged-in user
    private final AccessLevel accessLevel;

    public CourseController(CourseView courseView, CourseDAO courseDAO, TeacherDAO teacherDAO, AccessLevel accessLevel) {
        this.courseView = courseView;
        this.courseDAO = courseDAO;
        this.teacherDAO = teacherDAO;
        this.accessLevel = accessLevel;

        initialize();
    }

    private void initialize() {
        // Gets courses from the database
        // FIXME! GENERATES EXAMPLE COURSES IF NO DATABASE CONNECTION
        try {
            courseList.setAll(courseDAO.findAll());
        } catch(Exception e) {
            courseList.setAll(Course.generateExampleCourses());
        }

        // Adds courses to the table in CourseView using the filteredCourseList to allow searching
        courseView.getCourseTable().setItems(filteredCourseList);

        // Adds a listener to the search field and adds logic to allow searching the course table
        courseView.getSearchField().textProperty().addListener((observable, oldValue, newValue) -> {
            String searchQuery = (newValue == null || newValue.isEmpty()) ? "" : newValue.trim().toLowerCase();
            filteredCourseList.setPredicate(course ->
                    searchQuery.isEmpty() ||
                    Integer.toString(course.getId()).contains(searchQuery) ||
                    course.getCourseCode().toLowerCase().contains(searchQuery) ||
                    course.getTitle().toLowerCase().contains(searchQuery) ||
                    Integer.toString(course.getCredits()).contains(searchQuery) ||
                    Integer.toString(course.getTeacherId()).contains(searchQuery)
            );
        });

        updateComboBoxSelection();
        setUiElementAvailability();
        initButtonFunctionality();
        addListeners();
        addTableListener();
    }

    private void refresh() {
        try {
            courseList.setAll(courseDAO.findAll());
            updateComboBoxSelection();
        } catch(Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while trying to refresh. No database connection.");
        }
    }

    // Update ComboBoxes with items from the database
    private void updateComboBoxSelection() {
        try {
            courseView.getTeacherIdComboBox().getSelectionModel().clearSelection();
            courseView.getTeacherIdComboBox().getItems().clear();
            teacherList.setAll(teacherDAO.findAll());
        } catch (Exception e) {
            // FIXME!
            teacherList.setAll(Teacher.generateExampleTeachers());
        }

        courseView.getTeacherIdComboBox().setItems(teacherList);
    }

    private void setUiElementAvailability() {
        // Show CRUD controls for courses only when logged in as an administrator
        if (accessLevel == AccessLevel.ADMIN) {
            courseView.getControlBox().setVisible(true);
        }
    }

    // Set button functionality
    private void initButtonFunctionality() {

        // Adds functionality to the clear search button in CourseView
        courseView.getClearSearchButton().setOnMouseClicked(event -> {
            courseView.getSearchField().clear();
        });

        // Adds functionality to the refresh button in CourseView
        courseView.getRefreshButton().setOnMouseClicked(event -> {
            refresh();
        });

        // Adds functionality to the edit button in CourseView
        courseView.getEditSelectedButton().setOnMouseClicked(event -> {
            Course selectedCourse = courseView.getCourseTable().getSelectionModel().getSelectedItem();

            var result = CourseDialog.editCourseDialog(selectedCourse, teacherList);

            if (result.isPresent()) {
                var resultCourse = result.get();

                try {
                    courseDAO.updateSingle(resultCourse);
                    refresh();
                } catch (Exception e) {
                    AlertUtil.showDatabaseConnectionError("Error updating course. No database connection.");
                }
            }
        });

        // Adds functionality to the delete button in CourseView
        courseView.getDeleteSelectedButton().setOnMouseClicked(event -> {
            ObservableList<Course> selectedCourses = courseView.getCourseTable().getSelectionModel().getSelectedItems();

            String alertTitleHeader = "Confirm deletion";
            String plural = selectedCourses.size() == 1 ? "" : "s";
            String alertContent = "Are you sure you want to delete " + selectedCourses.size() + " course" + plural + "?";

            if (AlertUtil.confirmationAlert(alertTitleHeader, alertTitleHeader, alertContent)) {
                try {
                    courseDAO.deleteList(selectedCourses);
                    refresh();
                } catch (Exception e) {
                    AlertUtil.showDatabaseConnectionError("Error deleting course" + plural + ". No database connection.");
                }
            }
        });

        // Adds functionality to the add button in CourseView
        courseView.getAddButton().setOnMouseClicked(event -> {
            Course courseToBeAdded = new Course(
                    courseView.getCourseCodeField().getText().trim(),
                    courseView.getTitleField().getText().trim(),
                    Integer.parseInt(courseView.getCreditsField().getText().trim()),
                    courseView.getTeacherIdComboBox().getValue().getId()
            );

            try {
                courseDAO.addSingle(courseToBeAdded);
                courseView.getCourseCodeField().clear();
                courseView.getTitleField().clear();
                courseView.getCreditsField().clear();
                courseView.getTeacherIdComboBox().getSelectionModel().clearSelection();
                refresh();
            } catch (Exception e) {
                AlertUtil.showDatabaseConnectionError("Error adding course. No database connection.");
            }
        });

    }

    private void addListeners() {
        var addButton = courseView.getAddButton();
        var courseCodeField = courseView.getCourseCodeField();
        var titleField = courseView.getTitleField();
        var creditsField = courseView.getCreditsField();
        var teacherIdComboBox = courseView.getTeacherIdComboBox();

        Runnable updateButtonState = () -> {
            boolean disableButton =
                    courseCodeField.getText().isEmpty() ||
                    titleField.getText().isEmpty() ||
                    creditsField.getText().isEmpty() ||
                    teacherIdComboBox.getSelectionModel().isEmpty();

            addButton.setDisable(disableButton);
        };

        // Listener for TextFields that updates addButton's disable property when textProperty changes
        ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> updateButtonState.run();

        courseCodeField.textProperty().addListener(textFieldListener);
        titleField.textProperty().addListener(textFieldListener);
        creditsField.textProperty().addListener(textFieldListener);

        // Listener for teacherIdComboBox that updates addButton when ComboBox selection changes
        teacherIdComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateButtonState.run();
        });

        // Listener that prevents non-digits from being entered into creditsField
        creditsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                creditsField.setText(oldValue);
            }
        });
    }

    private void addTableListener() {
        var table = courseView.getCourseTable();
        var editButton = courseView.getEditSelectedButton();
        var deleteButton =  courseView.getDeleteSelectedButton();

        var tableSelectionModel =  table.getSelectionModel();

        // Adds a listener to courseTable that runs code whenever a row is selected/deselected
        tableSelectionModel.selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            var selectedCount = tableSelectionModel.getSelectedItems().size();

            // Enables the edit button only when exactly 1 row is selected
            editButton.setDisable(selectedCount != 1);

            // Enables the delete button if at least one row is selected
            deleteButton.setDisable(selectedCount == 0);
        });
    }

}
