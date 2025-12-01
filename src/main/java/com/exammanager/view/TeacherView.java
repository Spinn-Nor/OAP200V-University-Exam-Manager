package com.exammanager.view;

import com.exammanager.model.Department;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import com.exammanager.model.Teacher;

/**
 * A view for managing teachers.
 * <p>
 * Provides a user interface with:
 * <ul>
 *     <li>A title bar and a refresh button</li>
 *     <li>A search bar and clear-search button</li>
 *     <li>A sortable table displaying teacher information (ID, first name, last name,
 *     department and email address)</li>
 *     <li>Buttons for editing and deleting teachers</li>
 *     <li>A form for adding new teachers</li>
 * </ul>
 * <p>
 * The view exposes getter methods for UI components so that the {@link com.exammanager.controller.TeacherController}
 * can bind functionality and handle user interaction.
 *
 * @author Bendik
 */
public class TeacherView extends VBox {

    private TableView<Teacher> teacherTable;
    private TextField searchField;
    private Button clearSearchButton;
    private Button refreshButton;
    private TextField firstNameField;
    private TextField lastNameField;
    private ComboBox<Department> departmentComboBox;
    private TextField emailField;
    private Button editSelectedButton;
    private Button deleteSelectedButton;
    private Button addButton;

    private VBox controlBox;
    private GridPane addForm;

    public TeacherView() {
        setSpacing(10);
        setPadding(new Insets(15));

        // Creates a label with the view's title
        Label title = new Label("Teachers");
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

        // Teacher table
        teacherTable = new TableView<>();
        teacherTable.setPrefHeight(720);
        teacherTable.setPrefWidth(800);
        teacherTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        teacherTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Defines teacherTable columns
        TableColumn<Teacher, Integer> idCol = new TableColumn<>("ID");
        idCol.setPrefWidth(50);
        idCol.setCellValueFactory(
                data -> new SimpleIntegerProperty(data.getValue().getId()).asObject()
        );

        TableColumn<Teacher, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setPrefWidth(150);
        firstNameCol.setCellValueFactory(
                data ->  new SimpleStringProperty(data.getValue().getFirstName())
        );

        TableColumn<Teacher, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setPrefWidth(150);
        lastNameCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getLastName())
        );

        TableColumn<Teacher, String> depCol = new TableColumn<>("Department");
        depCol.setPrefWidth(150);
        depCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getDepartment())
        );

        TableColumn<Teacher, String> emailCol = new TableColumn<>("Email");
        emailCol.setPrefWidth(200);
        emailCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getEmail())
        );

        // Adds columns to teacherTable
        teacherTable.getColumns().addAll(idCol, firstNameCol, lastNameCol, depCol, emailCol);

        // Creates a separator line between the table and the controls
        Separator tableSeparator = new Separator(Orientation.VERTICAL);

        // Defines control buttons
        editSelectedButton = new Button("Edit");
        editSelectedButton.setDisable(true);

        deleteSelectedButton = new Button("Delete");
        deleteSelectedButton.setDisable(true);

        // Creates a form for adding new teachers
        addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        addForm.add(new Label("First Name:"), 0, 0);
        firstNameField = new TextField();
        addForm.add(firstNameField, 1, 0);

        addForm.add(new Label("Last Name:"), 0, 1);
        lastNameField = new TextField();
        addForm.add(lastNameField, 1, 1);

        addForm.add(new Label("Department:"), 0, 2);
        departmentComboBox = new ComboBox<>();
        departmentComboBox.setPrefWidth(149);
        addForm.add(departmentComboBox, 1, 2);

        addForm.add(new Label("Email:"), 0, 3);
        emailField = new TextField();
        addForm.add(emailField, 1, 3);

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
        mainContainer.getChildren().addAll(teacherTable, tableSeparator, controlBox);

        // Table search bar
        HBox searchBox = new HBox();
        searchBox.setMaxWidth(800);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setSpacing(10);

        Label searchLabel = new Label("Search:");

        searchField = new TextField();
        searchField.setPrefWidth(620);
        searchField.setPromptText("Search for ID, first name, last name, department or email");

        clearSearchButton = new Button("Clear search");

        searchBox.setPrefWidth(teacherTable.getWidth());
        searchBox.getChildren().addAll(searchLabel, searchField, clearSearchButton);

        // Add everything to the view
        getChildren().addAll(topBar, topbarSeparator, searchBox, mainContainer);
    }

    public TableView<Teacher> getTeacherTable() {
        return teacherTable;
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

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public TextField getLastNameField() {
        return lastNameField;
    }

    public ComboBox<Department> getDepartmentComboBox() {
        return departmentComboBox;
    }

    public TextField getEmailField() {
        return emailField;
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

    public GridPane getAddForm() {
        return addForm;
    }
}
