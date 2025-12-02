package com.exammanager.dialog;

import com.exammanager.model.Department;
import com.exammanager.util.AlertUtil;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.util.Optional;

/**
 * A class for creating a dialog window for updating a department .
 * <p>
 * Contains a method which takes a Department object as an argument, and
 * displays a dialog window where the user can edit the department's name.
 *
 * @author Bendik
 */
public class DepartmentDialog {

    private static TextField textFieldDepartmentName;
    private static ButtonType saveButton;
    private static Label emptyFieldWarning;

    /**
     * Creates a modal dialog window for updating a course.
     * <p>
     * @param department the department to update
     * @return returns a {@link Optional} containing the updated department if saved,
     * otherwise returns an empty {@link Optional}
     */
    public static Optional<Department> editDepartmentDialog(Department department) {
        Dialog<Department> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        // dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setHeight(300);
        dialog.setWidth(400);
        dialog.setTitle("Edit Department");
        dialog.setHeaderText("Editing department: " + department.getName());
        dialog.resizableProperty().setValue(false);

        // dialog content
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // department name label & text field
        Label firstNameLabel = new Label("Department Name:");
        gridPane.add(firstNameLabel, 0, 0, 1, 1);
        textFieldDepartmentName = new TextField(department.getName());
        textFieldDepartmentName.setPrefWidth(200);
        textFieldDepartmentName.setPromptText("Cannot be empty");
        gridPane.add(textFieldDepartmentName, 1, 0, 4, 1);

        emptyFieldWarning = new Label();
        emptyFieldWarning.textProperty().set("Fields cannot be empty!");
        emptyFieldWarning.styleProperty().setValue("-fx-text-fill: red");
        emptyFieldWarning.setVisible(false);
        emptyFieldWarning.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(emptyFieldWarning, 1, 1, 2, 1);

        dialog.getDialogPane().setContent(gridPane);

        ButtonType closeButton = new ButtonType("Close", ButtonType.CLOSE.getButtonData());
        saveButton = new ButtonType("Save", ButtonType.APPLY.getButtonData());

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(saveButton, closeButton);
        addTextFieldListeners(dialogPane);

        // sets the return value of the dialog
        // returns a new Department object if save button is clicked
        // otherwise returns null
        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                // maps values from the text fields to variables
                String departmentName = textFieldDepartmentName.getText().trim();

                int departmentId = department.getId();

                // creates and returns a new department object constructed from the text field values
                return new Department(departmentId, departmentName);
            } else {
                return null;
            }
        });

        // Ask for confirmation when save button is clicked
        dialog.getDialogPane().lookupButton(saveButton).addEventFilter(ActionEvent.ACTION, event -> {
            if (AlertUtil.confirmationAlert("Confirm Save", "Confirm save", "Are you sure you want to save?")) {
                dialog.close();
            } else {
                event.consume();
            }
        });

        return dialog.showAndWait();
    }

    private static void addTextFieldListeners(DialogPane dialogPane) {
        Runnable updateButtonState = () -> {
            boolean emptyField =
                    textFieldDepartmentName.getText().trim().isEmpty();

            emptyFieldWarning.setVisible(emptyField);
            dialogPane.lookupButton(saveButton).setDisable(emptyField);
        };

        ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> updateButtonState.run();

        textFieldDepartmentName.textProperty().addListener(textFieldListener);
    }
}
