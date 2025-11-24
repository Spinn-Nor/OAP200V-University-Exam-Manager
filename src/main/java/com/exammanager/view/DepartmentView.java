package com.exammanager.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.exammanager.model.Department;

import java.util.Optional;

public class DepartmentView extends VBox {

    private TableView<Department> departmentTable;
    private TextField searchText;
    private Button clearSearchButton;
    private Button refreshButton;
    private TextField nameField;
    private Button addButton;
    private Button editSelectedButton;
    private Button deleteSelectedButton;

    private GridPane addForm;

    public DepartmentView() {
        setSpacing(10);
        setPadding(new Insets(15));

        // Title and refresh button
        Label title = new Label("Departments");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold");

        refreshButton = new Button("Refresh");

        Region topBarSpacer = new Region();
        HBox.setHgrow(topBarSpacer, Priority.ALWAYS);

        HBox topBar = new HBox(10, title, topBarSpacer, refreshButton);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Separator topbarSeparator = new Separator(Orientation.HORIZONTAL);


        // Main table
        departmentTable = new TableView<>();
        departmentTable.setPrefHeight(720);
        departmentTable.setPrefWidth(800);

        // fallback: hvis den konkrete policy ikke finnes i din JavaFX-versjon bytt til CONSTRAINED_RESIZE_POLICY
        try {
            departmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_NEXT_COLUMN);
        } catch (Exception e) {
            departmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        departmentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<Department, Integer> idCol = new TableColumn<>("ID");
        idCol.setPrefWidth(80);
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Department, String> nameCol = new TableColumn<>("Name");
        nameCol.setPrefWidth(200);
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        departmentTable.getColumns().addAll(idCol, nameCol);

        Separator tableSeparator = new Separator(Orientation.VERTICAL);

        // Buttons and form panel
        editSelectedButton = new Button("Edit");
        editSelectedButton.setDisable(true);

        deleteSelectedButton = new Button("Delete");
        deleteSelectedButton.setDisable(true);

        addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);


        addForm.add(new Label("Department Name:"), 0, 0);
        nameField = new TextField();
        addForm.add(nameField, 1, 0);

        addButton = new Button("Add");
        addButton.setDisable(true);
        addButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addForm.add(addButton, 0, 1, 2, 1);

        Region controlSpacer = new Region();
        VBox.setVgrow(controlSpacer, Priority.ALWAYS);

        VBox controlBox = new VBox(10, editSelectedButton, deleteSelectedButton, controlSpacer, addForm);
        controlBox.setPrefWidth(300);

        HBox mainContainer = new HBox(10, departmentTable, tableSeparator, controlBox);
        HBox.setHgrow(mainContainer, Priority.ALWAYS);


        // Search bar
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setSpacing(10);

        searchBox.getChildren().add(new Label("Search:"));

        searchText = new TextField();
        searchText.setPrefWidth(620);
        searchText.setPromptText("Search for department name");

        clearSearchButton = new Button("Clear search");

        searchBox.getChildren().addAll(searchText, clearSearchButton);

        // Add everything to the root layout
        getChildren().addAll(topBar, topbarSeparator, searchBox, mainContainer);

    }

    // 🔻 Getters så controller kan få tilgang til UI-komponentene 🔻

    public TableView<Department> getDepartmentTable() {
        return departmentTable;
    }

    public TextField getSearchText() {
        return searchText;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getClearSearchButton() {
        return clearSearchButton;
    }

    public TextField getNameField() {
        return nameField;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditSelectedButton() {
        return editSelectedButton;
    }

    public Button getDeleteSelectedButton() {
        return deleteSelectedButton;
    }

    public GridPane getAddForm() {
        return addForm;
    }

}