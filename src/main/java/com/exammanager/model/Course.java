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

    //Generate examples of Courses
    public static ObservableList<Course> generateExampleCourses() {
        ObservableList<Course> courseList = FXCollections.observableArrayList();

        Course course1 = new Course(1, "CS101", "Introduction to Programming", 10, 1);
        Course course2 = new Course(2, "MATH201", "Linear Algebra", 10, 2);
        Course course3 = new Course(3, "PHYS301", "Quantum Mechanics", 10, 1);
        Course course4 = new Course(4, "random4","HMM200V", 10, 3);
        Course course5 = new Course(5, "random5", "ELK3000", 10, 1);

        courseList.addAll(course1, course2, course3, course4, course5);

        return courseList;
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

}

