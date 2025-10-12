package com.exammanager.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StudentView extends VBox {

    public StudentView() {
        setSpacing(10);
        setPadding(new Insets(15));

        Label label = new Label("Students");

        setAlignment(Pos.CENTER);

        getChildren().addAll(label);
    }
}
