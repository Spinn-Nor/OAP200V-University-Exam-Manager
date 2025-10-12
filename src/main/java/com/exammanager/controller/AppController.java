package com.exammanager.controller;

import com.exammanager.view.MainView;
import com.exammanager.controller.TeacherController;

public class AppController {

    private MainView mainView;

    public AppController(MainView mainView) {
        this.mainView = mainView;
    }

    public void start() {
        // fixme! for testing
        new TeacherController(mainView.getTeacherView());
    }
}
