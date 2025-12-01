package com.exammanager.view;

import com.exammanager.model.Course;
import com.exammanager.model.Teacher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

//Victoria
public class CourseView extends VBox {

    private TableView<Course> courseTable;
    private TextField searchField;
    private Button clearSearchButton;
    private Button refreshButton;
    private TextField courseCodeField;
    private TextField titleField;
    private TextField creditsField;
    private ComboBox<Teacher> teacherIdComboBox;
    private Button editSelectedButton;
    private Button deleteSelectedButton;
    private Button addButton;

    private GridPane addForm;
    private VBox controlBox;

    //Victoria
    public CourseView() {
        setSpacing(10);
        setPadding(new Insets(15));

        // Creates a label with the view's title
        Label title = new Label("Courses");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold");

        // Creates a refresh button
        refreshButton = new Button("Refresh");

        // Creates growable empty space to push the refresh button to the right
        Region topBarSpacer = new Region();
        HBox.setHgrow(topBarSpacer, Priority.ALWAYS);

        // Creates a new box to layout elements horizontally
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Sets spacing for the topbar and adds the title and refresh button
        topBar.setSpacing(10);
        topBar.getChildren().addAll(title, topBarSpacer, refreshButton);

        // Creates a separator line between the topbar and the rest of the view
        Separator topbarSeparator = new Separator(Orientation.HORIZONTAL);

        HBox mainContainer = new HBox();
        HBox.setHgrow(mainContainer, Priority.ALWAYS);

        // Course table
        courseTable = new TableView<>();
        courseTable.setPrefHeight(720);
        courseTable.setPrefWidth(800);
        courseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        courseTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Victoria
        // Defines courseTable columns
        TableColumn<Course, Integer> course_idCol = new TableColumn<>("ID");
        course_idCol.setPrefWidth(50);
        course_idCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getId()).asObject()
        );

        TableColumn<Course, String> course_codeCol = new TableColumn<>("Course Code");
        course_codeCol.setPrefWidth(150);
        course_codeCol.setCellValueFactory(
                data ->  new SimpleStringProperty(data.getValue().getCourseCode())
        );

        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setPrefWidth(150);
        titleCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getTitle())
        );

        TableColumn<Course, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setPrefWidth(150);
        creditsCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getCredits()).asObject()
        );

        TableColumn<Course, Integer> teacher_idCol = new TableColumn<>("Teacher ID");
        teacher_idCol.setPrefWidth(200);
        teacher_idCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getTeacherId()).asObject()
        );

        //Victoria
        // Adds columns to courseTable
        courseTable.getColumns().addAll(course_idCol, course_codeCol, titleCol, creditsCol, teacher_idCol);

        // Creates a separator line between the table and the controls
        Separator tableSeparator = new Separator(Orientation.VERTICAL);

        // Defines control buttons
        editSelectedButton = new Button("Edit");
        editSelectedButton.setDisable(true);

        deleteSelectedButton = new Button("Delete");
        deleteSelectedButton.setDisable(true);

        //Victoria
        // Creates a form for adding new courses
        addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        addForm.add(new Label("Course code:"), 0, 0);
        courseCodeField = new TextField();
        addForm.add(courseCodeField, 1, 0);

        addForm.add(new Label("Title:"), 0, 1);
        titleField = new TextField();
        addForm.add(titleField, 1, 1);

        addForm.add(new Label("Credits:"), 0, 2);
        creditsField = new TextField();
        addForm.add(creditsField, 1, 2);

        addForm.add(new Label("Teacher:"), 0, 3);
        teacherIdComboBox = new ComboBox<>();
        teacherIdComboBox.setPrefWidth(149);
        addForm.add(teacherIdComboBox, 1, 3);

        addButton = new Button("Add");
        addButton.setDisable(true);
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addForm.add(addButton, 0, 4, 2, 1);

        //Victoria
        // Spacer to push add form to the bottom
        Region controlSpacer = new Region();
        VBox.setVgrow(controlSpacer, Priority.ALWAYS);

        controlBox = new VBox();
        controlBox.setSpacing(10);
        controlBox.getChildren().addAll(editSelectedButton, deleteSelectedButton, controlSpacer, addForm);
        controlBox.setVisible(false);

        mainContainer.setSpacing(10);
        mainContainer.getChildren().addAll(courseTable, tableSeparator, controlBox);

        //Victoria
        // Table search bar
        HBox searchBox = new HBox();
        searchBox.setPrefWidth(800);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setSpacing(10);

        Label searchLabel = new Label("Search:");

        searchField = new TextField();
        searchField.setPrefWidth(620);
        searchField.setPromptText("Search for courseID, course code, title, credits or teacherID");

        clearSearchButton = new Button("Clear search");

        searchBox.setPrefWidth(courseTable.getWidth());
        searchBox.getChildren().addAll(searchLabel, searchField, clearSearchButton);

        // Add everything to the view
        getChildren().addAll(topBar, topbarSeparator, searchBox, mainContainer);
    }

    //Victoria
    public TableView<Course> getCourseTable() {
        return courseTable;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getClearSearchButton() {
        return clearSearchButton;
    }

    public TextField getCourseCodeField() {
        return courseCodeField;
    }

    //Victoria
    public TextField getTitleField() {
        return titleField;
    }

    public TextField getCreditsField() {
        return creditsField;
    }

    public ComboBox<Teacher> getTeacherIdComboBox() {
        return teacherIdComboBox;
    }

    public Button getEditSelectedButton() {
        return editSelectedButton;
    }

    public Button getDeleteSelectedButton() {
        return deleteSelectedButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public GridPane getAddForm() {
        return addForm;
    }

    public VBox getControlBox() {
        return controlBox;
    }
}

