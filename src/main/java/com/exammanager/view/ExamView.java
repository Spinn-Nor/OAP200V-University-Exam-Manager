package com.exammanager.view;

import com.exammanager.model.Exam;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import com.exammanager.model.Teacher;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 * Can sort multiple columns at once by holding shift
 */
public class ExamView extends VBox {

    private TableView<Exam> examTable;
    private TextField studentIdField;
    private TextField courseIdField;
    private TextField examDateField;
    private TextField gradeField;
    private Button editSelectedButton;
    private Button deleteSelectedButton;
    private Button addButton;

    // TODO! LOCAL VARIABLES IN CONSTRUCTOR -> CLASS PROPERTIES
    public ExamView() {
        setSpacing(10);
        setPadding(new Insets(15));

        // Creates a label with the view's title
        Label title = new Label("Exams");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold");

        // Creates a refresh button
        Button refreshButton = new Button("Refresh");

        // TODO! REMOVE AFTER TESTING
        refreshButton.setOnAction(event -> {showTestDialog();});

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

        // TODO! Maybe this should be a different pane-type
        HBox mainContainer = new HBox();
        HBox.setHgrow(mainContainer, Priority.ALWAYS);

        // Table test
        examTable = new TableView<>();
        examTable.setPrefHeight(720);
        examTable.setPrefWidth(800);
        examTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        examTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Defines examTable columns
        TableColumn<Exam, Integer> idCol = new TableColumn<>("Student Id");
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
                data -> new SimpleStringProperty(data.getValue().getExamDate())
        );

        TableColumn<Exam, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setPrefWidth(150);
        gradeCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getGrade())
        );


        // Adds columns to examTable
        examTable.getColumns().addAll(idCol, studentIdCol, courseIdCol, examDateCol, gradeCol);

        // Creates a separator line between the table and the controls
        // TODO! Remove?
        Separator tableSeparator = new Separator(Orientation.VERTICAL);

        // Defines control buttons
        editSelectedButton = new Button("Edit");
        editSelectedButton.setDisable(true);

        deleteSelectedButton = new Button("Delete");
        deleteSelectedButton.setDisable(true);

        // TODO! DELETE AFTER TESTING
        editSelectedButton.setOnAction(event -> {
            var selected = examTable.getSelectionModel().getSelectedItem();
            System.out.println(selected.getStudentId());
            System.out.println(selected.getCourseId());
            System.out.println(selected.getExamDate());
            System.out.println(selected.getGrade());
        });

        // Separator line between edit/delete buttons and add form
        // Separator controlSeparator = new Separator(Orientation.HORIZONTAL);

        // Creates a form for adding new exams
        GridPane addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        addForm.add(new Label("Student Id:"), 0, 0);
        // TODO! DROPDOWN LIST OF IDS?
        studentIdField = new TextField();
        addForm.add(studentIdField, 1, 0);

        addForm.add(new Label("Course Id:"), 0, 1);
        courseIdField = new TextField();
        addForm.add(courseIdField, 1, 1);

        addForm.add(new Label("Exam Date:"), 0, 2);
        examDateField = new TextField();
        addForm.add(examDateField, 1, 2);

        addForm.add(new Label("Grade:"), 0, 3);
        TextField gradeField = new TextField();
        addForm.add(gradeField, 1, 3);

        addButton = new Button("Add");
        addButton.setDisable(true);
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addForm.add(addButton, 0, 5, 2, 1);

        // Spacer to push add form to the bottom
        Region controlSpacer = new Region();
        VBox.setVgrow(controlSpacer, Priority.ALWAYS);

        VBox controlBox = new VBox();
        controlBox.setSpacing(10);
        controlBox.getChildren().addAll(editSelectedButton, deleteSelectedButton, controlSpacer, addForm);

        mainContainer.setSpacing(10);
        mainContainer.getChildren().addAll(examTable, tableSeparator, controlBox);

        // Table search bar
        int searchBoxSpacing = 10;
        HBox searchBox = new HBox();
        searchBox.setPrefWidth(800);
        Label searchLabel = new Label("Search:");
        TextField searchText = new TextField();
        searchText.setPrefWidth(720);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setSpacing(searchBoxSpacing);
        // TODO! fix searchbar width
        searchBox.setPrefWidth(examTable.getWidth() - searchBoxSpacing - searchLabel.getWidth());
        searchBox.getChildren().addAll(searchLabel, searchText);

        getChildren().addAll(topBar, topbarSeparator, searchBox, mainContainer);
    }

    public static void showTestDialog() {
        Dialog dialog = new Dialog();
        dialog.initModality(Modality.APPLICATION_MODAL);
        // dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setHeight(300);
        dialog.setWidth(400);
        dialog.setTitle("Test Dialog");
        dialog.setHeaderText("This is a test dialog");
        dialog.resizableProperty().setValue(false);

        ButtonType closeButton = new ButtonType("Close");
        dialog.getDialogPane().getButtonTypes().addAll(closeButton, ButtonType.CANCEL);

        dialog.showAndWait();
    }

    public TableView<Exam> getExamTable() {
        return examTable;
    }

    public TextField getStudentIdField() {
        return studentIdField;
    }

    public TextField getCourseIdField() {
        return courseIdField;
    }

    public TextField getExamDateField() {
        return examDateField;
    }

    public TextField getGradeField() {
        return gradeField;
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
}
