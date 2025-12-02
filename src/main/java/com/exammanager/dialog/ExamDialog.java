package com.exammanager.dialog;

import com.exammanager.model.Exam;
import com.exammanager.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.time.LocalDate;
import java.util.Optional;

/**
 * A class for creating a dialog window for updating an exam.
 * <p>
 * Contains a method which takes an exam object as an argument, and
 * displays a dialog window where the user can edit the exam's grade.
 *
 * @author Bendik
 */
public class ExamDialog {

    private static ButtonType saveButton;

    /**
     * Creates a modal dialog window for updating an exam.
     * <p>
     * @param exam the exam to update
     * @return returns a {@link Optional} containing the updated exam if saved,
     * otherwise returns an empty {@link Optional}
     */
    public static Optional<Exam> editExamDialog(Exam exam) {
        Dialog<Exam> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        // dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setHeight(300);
        dialog.setWidth(400);
        dialog.setTitle("Edit Exam");
        dialog.setHeaderText("Editing exam " + exam.getId() + " for student " + exam.getStudentId());
        dialog.resizableProperty().setValue(false);

        // dialog content
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // grade label & combobox
        Label gradeLabel = new Label("Grade:");
        gridPane.add(gradeLabel, 0, 0, 1, 1);
        ComboBox<String> gradeComboBox =  new ComboBox<>();
        gradeComboBox.getItems().addAll("A", "B", "C", "D", "E", "F");
        gradeComboBox.setValue(exam.getGrade());
        gridPane.add(gradeComboBox, 1, 0, 4, 1);

        dialog.getDialogPane().setContent(gridPane);

        ButtonType closeButton = new ButtonType("Close", ButtonType.CLOSE.getButtonData());
        saveButton = new ButtonType("Save", ButtonType.APPLY.getButtonData());

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(saveButton, closeButton);

        // sets the return value of the dialog
        // returns a new Exam object if save button is clicked
        // otherwise returns null
        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                String grade = gradeComboBox.getValue();

                int examId = exam.getId();
                int studentId = exam.getStudentId();
                int courseId = exam.getCourseId();
                LocalDate examDate = exam.getExamDate();

                // creates and returns a new exam object constructed from the text field values
                return new Exam(examId, studentId, courseId, examDate, grade);
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
}
