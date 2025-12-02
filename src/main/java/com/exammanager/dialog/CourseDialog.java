package com.exammanager.dialog;

import com.exammanager.model.Course;
import com.exammanager.model.Teacher;
import com.exammanager.util.AlertUtil;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.util.Optional;

/**
 * A class for creating a dialog window for updating a course.
 * <p>
 * Contains a method which takes a Course object as an argument, and
 * displays a dialog window where the user can edit the course's code,
 * title, credit and teacherId.
 *
 * @author Bendik
 */
public class CourseDialog {

    private static TextField textFieldCourseCode;
    private static TextField textFieldTitle;
    private static TextField textFieldCredits;
    private static ComboBox<Teacher> teacherIdComboBox;
    private static ButtonType saveButton;
    private static Label emptyFieldWarning;

    /**
     * Creates a modal dialog window for updating a course.
     * <p>
     * @param course the course to update
     * @param teacherList a list of available teachers
     * @return returns a {@link Optional} containing the updated course if saved,
     * otherwise returns an empty {@link Optional}
     */
    public static Optional<Course> editCourseDialog(Course course, ObservableList<Teacher> teacherList) {
        Dialog<Course> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        // dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setHeight(300);
        dialog.setWidth(400);
        dialog.setTitle("Edit course");
        dialog.setHeaderText("Editing course: " + course.getId() + " - " + course.getTitle());
        dialog.resizableProperty().setValue(false);

        // dialog content
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // course code label & text field
        Label courseCodeLabel = new Label("Course Code:");
        gridPane.add(courseCodeLabel, 0, 0, 1, 1);
        textFieldCourseCode = new TextField(course.getCourseCode());
        textFieldCourseCode.setPrefWidth(200);
        textFieldCourseCode.setPromptText("Cannot be empty");
        gridPane.add(textFieldCourseCode, 1, 0, 4, 1);

        // title label & text field
        Label titleLabel = new Label("Title:");
        gridPane.add(titleLabel, 0, 1, 1, 1);
        textFieldTitle = new TextField(course.getTitle());
        textFieldTitle.setPromptText("Cannot be empty");
        gridPane.add(textFieldTitle, 1, 1, 4, 1);

        // credits label & text field
        Label creditsLabel = new Label("Credits:");
        gridPane.add(creditsLabel, 0, 2, 1, 1);
        textFieldCredits = new TextField(Integer.toString(course.getCredits()));
        textFieldCredits.setPromptText("Cannot be empty");
        gridPane.add(textFieldCredits, 1, 2, 4, 1);

        // teacher ID label & text field
        Label teacherIdLabel = new Label("Teacher ID:");
        gridPane.add(teacherIdLabel, 0, 3, 1, 1);
        teacherIdComboBox = new ComboBox<>();
        teacherIdComboBox.setPrefWidth(200);
        teacherIdComboBox.setItems(teacherList);
        teacherIdComboBox.setValue(teacherList.stream().filter(teacher -> teacher.getId() == course.getTeacherId()).findFirst().get());
        gridPane.add(teacherIdComboBox, 1, 3, 4, 1);

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
        addListeners(dialogPane);

        // sets the return value of the dialog
        // returns a new Student object if save button is clicked
        // otherwise returns null
        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                // maps values from the text fields to variables
                String courseCode = textFieldCourseCode.getText().trim();
                String title = textFieldTitle.getText().trim();
                int credits = Integer.parseInt(textFieldCredits.getText().trim());
                int teacherId = teacherIdComboBox.getValue().getId();

                int courseId = course.getId();

                // creates and returns a new course object constructed from the text field & combobox values
                return new Course(courseId, courseCode, title, credits, teacherId);
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

    private static void addListeners(DialogPane dialogPane) {
        Runnable updateButtonState = () -> {
            boolean emptyField =
                    textFieldCourseCode.getText().trim().isEmpty() ||
                    textFieldTitle.getText().trim().isEmpty() ||
                    textFieldCredits.getText().trim().isEmpty();

            emptyFieldWarning.setVisible(emptyField);
            dialogPane.lookupButton(saveButton).setDisable(emptyField);
        };

        ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> updateButtonState.run();

        textFieldCourseCode.textProperty().addListener(textFieldListener);
        textFieldTitle.textProperty().addListener(textFieldListener);
        textFieldCredits.textProperty().addListener(textFieldListener);

        // Listener that prevents anything but digits from being entered into the credits text field
        textFieldCredits.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textFieldCredits.setText(oldValue);
            }
        });
    }
}