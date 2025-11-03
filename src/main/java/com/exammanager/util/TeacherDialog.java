package com.exammanager.util;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.util.Optional;

import com.exammanager.model.Teacher;

public class TeacherDialog {

    private static TextField textFieldFirstName;
    private static TextField textFieldLastName;
    private static TextField textFieldEmail;
    private static ButtonType saveButton;
    private static Label emptyFieldWarning;

    public static Optional<Teacher> editTeacherDialog(Teacher teacher) {
        Dialog<Teacher> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        // dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setHeight(300);
        dialog.setWidth(400);
        dialog.setTitle("Edit Teacher");
        dialog.setHeaderText("Editing teacher: " + teacher.getFirstName() + " " + teacher.getLastName());
        dialog.resizableProperty().setValue(false);

        // dialog content
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // first name label & text field
        Label firstNameLabel = new Label("First Name:");
        gridPane.add(firstNameLabel, 0, 0, 1, 1);
        textFieldFirstName = new TextField(teacher.getFirstName());
        textFieldFirstName.setPrefWidth(200);
        textFieldFirstName.setPromptText("Cannot be empty");
        gridPane.add(textFieldFirstName, 1, 0, 4, 1);

        // last name label & text field
        Label lastNameLabel = new Label("Last Name:");
        gridPane.add(lastNameLabel, 0, 1, 1, 1);
        textFieldLastName = new TextField(teacher.getLastName());
        textFieldLastName.setPromptText("Cannot be empty");
        gridPane.add(textFieldLastName, 1, 1, 4, 1);

        // TODO! CHANGE TO COMBOBOX
        Label departmentLabel = new Label("Department:");
        gridPane.add(departmentLabel, 0, 2, 1, 1);
        TextField textFieldDepartment = new TextField(teacher.getDepartment());
        gridPane.add(textFieldDepartment, 1, 2, 4, 1);

        // email label & text field
        Label emailLabel = new Label("Email:");
        gridPane.add(emailLabel, 0, 3, 1, 1);
        textFieldEmail = new TextField(teacher.getEmail());
        textFieldEmail.setPromptText("Cannot be empty");
        gridPane.add(textFieldEmail, 1, 3, 4, 1);

        emptyFieldWarning = new Label();
        emptyFieldWarning.textProperty().set("Fields cannot be empty!");
        emptyFieldWarning.styleProperty().setValue("-fx-text-fill: red");
        emptyFieldWarning.setVisible(false);
        emptyFieldWarning.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(emptyFieldWarning, 1, 4, 2, 1);

        dialog.getDialogPane().setContent(gridPane);

        ButtonType closeButton = new ButtonType("Close", ButtonType.CLOSE.getButtonData());
        saveButton = new ButtonType("Save", ButtonType.APPLY.getButtonData());

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(saveButton, closeButton);
        addTextFieldListeners(dialogPane);

        // sets the return value of the dialog
        // returns a new Teacher object if save button is clicked
        // otherwise returns null
        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                // maps values from the text fields to variables
                String firstName = textFieldFirstName.getText().trim();
                String lastName = textFieldLastName.getText().trim();
                String department = textFieldDepartment.getText().trim();
                String email = textFieldEmail.getText().trim();

                // creates and returns a new teacher object constructed from the text field values
                return new Teacher(firstName, lastName, department, email);
            } else {
                return null;
            }
        });

        return dialog.showAndWait();
    }

    private static void addTextFieldListeners(DialogPane dialogPane) {
        Runnable updateButtonState = () -> {
            boolean emptyField =
                textFieldFirstName.getText().trim().isEmpty() ||
                textFieldLastName.getText().trim().isEmpty() ||
                textFieldEmail.getText().trim().isEmpty();

            emptyFieldWarning.setVisible(emptyField);
            dialogPane.lookupButton(saveButton).setDisable(emptyField);
        };

        ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> updateButtonState.run();

        textFieldFirstName.textProperty().addListener(textFieldListener);
        textFieldLastName.textProperty().addListener(textFieldListener);
        textFieldEmail.textProperty().addListener(textFieldListener);
    }
}
