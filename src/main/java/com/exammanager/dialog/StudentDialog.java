package com.exammanager.dialog;

import com.exammanager.util.AlertUtil;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.util.Optional;

import com.exammanager.model.Student;

/**
 * A class for creating a dialog window for updating a student.
 * <p>
 * Contains a method which takes a student object as an argument, and
 * displays a dialog window where the user can edit the student's first name,
 * last name, email address and enrollment year.
 *
 * @author Bendik
 */
public class StudentDialog {

    private static TextField textFieldFirstName;
    private static TextField textFieldLastName;
    private static TextField textFieldEmail;
    private static TextField textFieldEnrollmentYear;
    private static ButtonType saveButton;
    private static Label emptyFieldWarning;

    /**
     * Creates a modal dialog window for updating a student.
     * <p>
     * @param student the student to update
     * @return returns a {@link Optional} containing the updated student if saved,
     * otherwise returns an empty {@link Optional}
     */
    public static Optional<Student> editStudentDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        // dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setHeight(300);
        dialog.setWidth(400);
        dialog.setTitle("Edit student");
        dialog.setHeaderText("Editing student: " + student.getFirstName() + " " + student.getLastName());
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
        textFieldFirstName = new TextField(student.getFirstName());
        textFieldFirstName.setPrefWidth(200);
        textFieldFirstName.setPromptText("Cannot be empty");
        gridPane.add(textFieldFirstName, 1, 0, 4, 1);

        // last name label & text field
        Label lastNameLabel = new Label("Last Name:");
        gridPane.add(lastNameLabel, 0, 1, 1, 1);
        textFieldLastName = new TextField(student.getLastName());
        textFieldLastName.setPromptText("Cannot be empty");
        gridPane.add(textFieldLastName, 1, 1, 4, 1);

        // email label & text field
        Label emailLabel = new Label("Email:");
        gridPane.add(emailLabel, 0, 2, 1, 1);
        textFieldEmail = new TextField(student.getEmail());
        textFieldEmail.setPromptText("Cannot be empty");
        gridPane.add(textFieldEmail, 1, 2, 4, 1);

        // enrollment year label & text field
        Label enrollmentYearLabel = new Label("Enrollment Year:");
        gridPane.add(enrollmentYearLabel, 0, 3, 1, 1);
        textFieldEnrollmentYear = new TextField(String.valueOf(student.getEnrollmentYear()));
        textFieldEnrollmentYear.setPromptText("Cannot be empty");
        gridPane.add(textFieldEnrollmentYear, 1, 3, 4, 1);

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
        // returns a new Student object if save button is clicked
        // otherwise returns null
        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                // maps values from the text fields to variables
                String firstName = textFieldFirstName.getText().trim();
                String lastName = textFieldLastName.getText().trim();
                String email = textFieldEmail.getText().trim();
                int enrollmentYear = Integer.parseInt(textFieldEnrollmentYear.getText().trim());

                int studentId = student.getId();

                // creates and returns a new student object constructed from the text field values
                return new Student(studentId, firstName, lastName, email, enrollmentYear);
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
                    textFieldFirstName.getText().trim().isEmpty() ||
                    textFieldLastName.getText().trim().isEmpty() ||
                    textFieldEmail.getText().trim().isEmpty() ||
                    textFieldEnrollmentYear.getText().trim().isEmpty();

            emptyFieldWarning.setVisible(emptyField);
            dialogPane.lookupButton(saveButton).setDisable(emptyField);
        };

        ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> updateButtonState.run();

        // Listener that prevents anything but digits from being entered into the enrollment year text field
        ChangeListener<String> enrollmentYearNonDigitListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textFieldEnrollmentYear.setText(oldValue);
            }
        };

        textFieldFirstName.textProperty().addListener(textFieldListener);
        textFieldLastName.textProperty().addListener(textFieldListener);
        textFieldEmail.textProperty().addListener(textFieldListener);
        textFieldEnrollmentYear.textProperty().addListener(textFieldListener);
        textFieldEnrollmentYear.textProperty().addListener(enrollmentYearNonDigitListener);
    }

}
