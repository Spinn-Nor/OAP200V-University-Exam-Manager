package com.exammanager.view;

import com.exammanager.model.Course;
import com.exammanager.model.Exam;
import com.exammanager.model.Student;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

/**
 * Can sort multiple columns at once by holding shift
 */
public class ExamView extends VBox {

    private TableView<Exam> examTable;
    private TextField searchField;
    private Button clearSearchButton;
    private Button refreshButton;
    private ComboBox<Student> studentIdComboBox;
    private ComboBox<Course> courseIdComboBox;
    private DatePicker examDatePicker;
    private ComboBox<String> gradeComboBox;
    private Button editSelectedButton;
    private Button deleteSelectedButton;
    private Button addButton;
    private VBox controlBox;

    public ExamView() {
        setSpacing(10);
        setPadding(new Insets(15));

        // Creates a label with the view's title
        Label title = new Label("Exams");
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

        // Table test
        examTable = new TableView<>();
        examTable.setPrefHeight(720);
        examTable.setPrefWidth(800);
        examTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        examTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Defines examTable columns
        TableColumn<Exam, Integer> idCol = new TableColumn<>("Exam Id");
        idCol.setPrefWidth(50);
        idCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getId()).asObject()
        );

        TableColumn<Exam, Integer> studentIdCol = new TableColumn<>("Student Id");
        studentIdCol.setPrefWidth(50);
        studentIdCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getStudentId()).asObject()
        );

        TableColumn<Exam, Integer> courseIdCol = new TableColumn<>("Course Id");
        courseIdCol.setPrefWidth(150);
        courseIdCol.setCellValueFactory(
                data ->  new SimpleIntegerProperty(data.getValue().getCourseId()).asObject()
        );

        TableColumn<Exam, String> examDateCol = new TableColumn<>("Exam Date");
        examDateCol.setPrefWidth(150);
        examDateCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getExamDate().toString())
        );

        TableColumn<Exam, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setPrefWidth(150);
        gradeCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getGrade())
        );


        // Adds columns to examTable
        examTable.getColumns().addAll(idCol, studentIdCol, courseIdCol, examDateCol, gradeCol);

        // Creates a separator line between the table and the controls
        Separator tableSeparator = new Separator(Orientation.VERTICAL);

        // Defines control buttons
        editSelectedButton = new Button("Edit");
        editSelectedButton.setDisable(true);

        deleteSelectedButton = new Button("Delete");
        deleteSelectedButton.setDisable(true);

        // Creates a form for adding new exams
        GridPane addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        addForm.add(new Label("Student Id:"), 0, 0);
        studentIdComboBox = new ComboBox<>();
        studentIdComboBox.setPrefWidth(220);
        addForm.add(studentIdComboBox, 1, 0);

        addForm.add(new Label("Course Id:"), 0, 1);
        courseIdComboBox = new ComboBox<>();
        courseIdComboBox.setPrefWidth(220);
        addForm.add(courseIdComboBox, 1, 1);

        addForm.add(new Label("Exam Date:"), 0, 2);
        examDatePicker = new DatePicker();
        examDatePicker.setPrefWidth(220);
        examDatePicker.setShowWeekNumbers(true);
        examDatePicker.setValue(LocalDate.now());
        addForm.add(examDatePicker, 1, 2);

        addForm.add(new Label("Grade:"), 0, 3);
        gradeComboBox = new ComboBox<>();
        gradeComboBox.getItems().addAll("A",  "B", "C", "D", "E", "F", "No grade");
        gradeComboBox.setValue("No grade");
        addForm.add(gradeComboBox, 1, 3);

        addButton = new Button("Add");
        addButton.setDisable(true);
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addForm.add(addButton, 0, 4, 2, 1);

        // Spacer to push add form to the bottom
        Region controlSpacer = new Region();
        VBox.setVgrow(controlSpacer, Priority.ALWAYS);

        controlBox = new VBox();
        controlBox.setSpacing(10);
        controlBox.getChildren().addAll(editSelectedButton, deleteSelectedButton, controlSpacer, addForm);
        controlBox.setVisible(false);

        mainContainer.setSpacing(10);
        mainContainer.getChildren().addAll(examTable, tableSeparator, controlBox);

        // Table search bar
        HBox searchBox = new HBox();
        searchBox.setPrefWidth(800);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setSpacing(10);

        Label searchLabel = new Label("Search:");

        searchField = new TextField();
        searchField.setPrefWidth(620);
        searchField.setPromptText("Search for exam ID, course ID or student ID");

        clearSearchButton = new Button("Clear search");

        searchBox.setPrefWidth(examTable.getWidth());
        searchBox.getChildren().addAll(searchLabel, searchField, clearSearchButton);

        getChildren().addAll(topBar, topbarSeparator, searchBox, mainContainer);
    }

    public TableView<Exam> getExamTable() {
        return examTable;
    }

    public ComboBox<Student> getStudentIdComboBox() {
        return studentIdComboBox;
    }

    public ComboBox<Course> getCourseIdComboBox() {
        return courseIdComboBox;
    }

    public DatePicker getExamDatePicker() {
        return examDatePicker;
    }

    public ComboBox<String> getGradeComboBox() {
        return gradeComboBox;
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

    public VBox getControlBox() {
        return controlBox;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getClearSearchButton() {
        return clearSearchButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

}
