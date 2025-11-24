package com.exammanager.controller;

import com.exammanager.dao.DepartmentDAO;
import com.exammanager.dialog.DepartmentDialog;
import com.exammanager.login.AccessLevel;
import com.exammanager.model.Department;
import com.exammanager.util.AlertUtil;
import com.exammanager.view.DepartmentView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * @author Anwar
 */
public class DepartmentController {

    private final DepartmentView departmentView;

    private final ObservableList<Department> departmentList = FXCollections.observableArrayList();

    private final FilteredList<Department> filteredDepartmentList =
            new FilteredList<>(departmentList, p -> true);

    private final DepartmentDAO departmentDAO;

    // TODO!
    private AccessLevel accessLevel;

    public DepartmentController(DepartmentView view, DepartmentDAO departmentDAO) {
        this.departmentView = view;
        this.departmentDAO = departmentDAO;
        initialize();
    }


    private void initialize() {

        // Hent data ved oppstart
        // FIXME! GENERATES EXAMPLE DEPARTMENTS IF NO DATABASE CONNECTION
        try {
            departmentList.setAll(departmentDAO.findAll());
        } catch(Exception e) {
            departmentList.setAll(Department.generateExampleDepartments());
        }

        // Koble tabellen til filtered list
        departmentView.getDepartmentTable().setItems(filteredDepartmentList);

        // Søkefunksjon
        departmentView.getSearchText().textProperty().addListener((obs, oldValue, newValue) -> {
            String query = newValue.toLowerCase().trim();
            filteredDepartmentList.setPredicate(dept ->
                    query.isEmpty() ||
                            dept.getName().toLowerCase().contains(query)
            );
        });

        // Sett opp UI-interaksjoner
        initButtonFunctionality();
        initTextFieldListener();
        initTableListener();
    }

    // Bendik
    // TODO!
    private void setUiElementAvailability(/*AccessLevel accessLevel*/) {}

    private void initButtonFunctionality() {

        // Refresh
        departmentView.getRefreshButton().setOnAction(e -> refreshDepartmentTable());

        // Clear search
        departmentView.getClearSearchButton().setOnAction(e -> departmentView.getSearchText().clear());

        // Edit
        departmentView.getEditSelectedButton().setOnAction(e -> {
            Department selected = departmentView.getDepartmentTable().getSelectionModel().getSelectedItem();

            var result = DepartmentDialog.editDepartmentDialog(selected);

            result.ifPresent(updated -> {
                try {
                    departmentDAO.updateSingle(updated);
                    refreshDepartmentTable();
                } catch (Exception ex) {
                    AlertUtil.showDatabaseConnectionError("Error updating department.");
                }
            });
        });


        // Delete
        departmentView.getDeleteSelectedButton().setOnAction(e -> {
            var selectedList =
                    departmentView.getDepartmentTable().getSelectionModel().getSelectedItems();

            String alertTitleHeader = "Confirm deletion";
            String plural = selectedList.size() == 1 ? "" : "s";
            String alertContent = "Are you sure you want to delete " + selectedList.size() + " department" + plural + "?";

            if (AlertUtil.confirmationAlert(alertTitleHeader, alertTitleHeader, alertContent)) {
                try {
                    departmentDAO.deleteList(selectedList);
                    refreshDepartmentTable();
                } catch (Exception ex) {
                    AlertUtil.showDatabaseConnectionError("Error deleting department(s).");
                }
            }
        });

        // Add new department
        departmentView.getAddButton().setOnAction(e -> {

            String name = departmentView.getNameField().getText().trim();
            if (name.isEmpty()) return;

            Department newDept = new Department(name);

            try {
                departmentDAO.addSingle(newDept);
                departmentView.getNameField().clear();
                refreshDepartmentTable();
            } catch (Exception ex) {
                AlertUtil.showDatabaseConnectionError("Error adding department.");
            }
        });
    }

    private void initTextFieldListener() {
        var nameField = departmentView.getNameField();
        var addButton = departmentView.getAddButton();

        nameField.textProperty().addListener((obs, oldVal, newVal) ->
                addButton.setDisable(newVal.trim().isEmpty())
        );
    }

    private void refreshDepartmentTable() {
        try {
            departmentList.setAll(departmentDAO.findAll());
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error refreshing departments.");
        }
    }

    private void initTableListener() {
        var table = departmentView.getDepartmentTable();
        var editBtn = departmentView.getEditSelectedButton();
        var deleteBtn = departmentView.getDeleteSelectedButton();

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            int count = table.getSelectionModel().getSelectedItems().size();

            editBtn.setDisable(count != 1);
            deleteBtn.setDisable(count == 0);

        });

    }

}