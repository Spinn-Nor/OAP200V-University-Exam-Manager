package com.exammanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Victoria
public class Course {
    private int id;
    private String courseCode;
    private String title;
    private int credits;
    private int teacherId;

    //Victoria
    public Course(int id, String courseCode, String title, int credits, int teacherId) {
        this.id = id;
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.teacherId = teacherId;
    }

    //Victoria
    public Course(String courseCode, String title, int credits, int teacherId) {
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.teacherId = teacherId;
    }

    //Getters (Reach data and sees what is inside. It looks inside the box.)

    //Victoria
    public int getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public int getTeacherId() {
        return teacherId;
    }

    //Victoria
    //Setters (Change data. Put something in the box.)
    public void setId(int courseId) {
        this.id = id;
    }

    public void setCourse_code(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return id + " - " + title;
    }

}

