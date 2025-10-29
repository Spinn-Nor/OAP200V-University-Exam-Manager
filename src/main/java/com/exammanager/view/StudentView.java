package com.exammanager.view;

import com.exammanager.model.Student;
import com.exammanager.model.Teacher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;

//Victoria
public class StudentView extends VBox {

    private TableView<Student> studentTable;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField enrollmentYearField;
    private Button editSelectedButton;
    private Button deleteSelectedButton;
    private Button addButton;

    //Victoria
    // TODO! LOCAL VARIABLES IN CONSTRUCTOR -> CLASS PROPERTIES
    public StudentView() {
        setSpacing(10);
        setPadding(new Insets(15));

        // Creates a label with the view's title
        Label title = new Label("Students");
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
        studentTable = new TableView<>();
        studentTable.setPrefHeight(720);
        studentTable.setPrefWidth(800);
        studentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        studentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Defines teacherTable columns
        TableColumn<Student, Integer> idCol = new TableColumn<>("ID");
        idCol.setPrefWidth(50);
        idCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getId()).asObject()
        );

        TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setPrefWidth(150);
        firstNameCol.setCellValueFactory(
                data ->  new SimpleStringProperty(data.getValue().getFirstName())
        );

        TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setPrefWidth(150);
        lastNameCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getLastName())
        );

        TableColumn<Student, Integer> enrollmentYearCol = new TableColumn<>("Enrollment Year");
        enrollmentYearCol.setPrefWidth(150);
        enrollmentYearCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getEnrollmentYear()).asObject()
        );

        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setPrefWidth(200);
        emailCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getEmail())
        );

        //Victoria
        // Adds columns to teacherTable
        studentTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, enrollmentYearCol, emailCol);

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
            var selected = studentTable.getSelectionModel().getSelectedItem();
            System.out.println(selected.getId());
            System.out.println(selected.getFirstName());
            System.out.println(selected.getLastName());
            System.out.println(selected.getEmail());
            System.out.println(selected.getEnrollmentYear());
        });

        // Separator line between edit/delete buttons and add form
//        Separator controlSeparator = new Separator(Orientation.HORIZONTAL);

        //Victoria
        // Creates a form for adding new teachers
        GridPane addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        addForm.add(new Label("First Name:"), 0, 0);
        firstNameField = new TextField();
        addForm.add(firstNameField, 1, 0);

        addForm.add(new Label("Last Name:"), 0, 1);
        lastNameField = new TextField();
        addForm.add(lastNameField, 1, 1);

        // FIXME! DEPARTMENT SHOULD USE A DROPDOWN BOX
        addForm.add(new Label("Email:"), 0, 3);
        emailField = new TextField();
        addForm.add(emailField, 1, 3);

        addForm.add(new Label("Enrollment Year:"), 0, 2);
        TextField depField = new TextField();
        addForm.add(depField, 1, 2);

        addButton = new Button("Add");
        addButton.setDisable(true);
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addForm.add(addButton, 0, 4, 2, 1);

        // Spacer to push add form to the bottom
        Region controlSpacer = new Region();
        VBox.setVgrow(controlSpacer, Priority.ALWAYS);

        VBox controlBox = new VBox();
        controlBox.setSpacing(10);
        controlBox.getChildren().addAll(editSelectedButton, deleteSelectedButton, controlSpacer, addForm);

        mainContainer.setSpacing(10);
        mainContainer.getChildren().addAll(studentTable, tableSeparator, controlBox);

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
        searchBox.setPrefWidth(studentTable.getWidth() - searchBoxSpacing - searchLabel.getWidth());
        searchBox.getChildren().addAll(searchLabel, searchText);

        getChildren().addAll(topBar, topbarSeparator, searchBox, mainContainer);
    }

    //Victoria
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

    //Victoria
    public TableView<Student> getStudentTable() {
        return studentTable;
    }

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public TextField getLastNameField() {
        return lastNameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getEnrollmentYearField() {return enrollmentYearField;}

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

