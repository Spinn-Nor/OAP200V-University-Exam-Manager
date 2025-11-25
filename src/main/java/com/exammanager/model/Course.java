package com.exammanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Victoria
public class Course {
    private int course_id;
    private String course_code;
    private String title;
    private int credits;
    private int teacher_id;

    //Victoria
    public Course(int course_id, String course_code, String title, int credits, int teacher_id) {
        this.course_id = course_id;
        this.course_code = course_code;
        this.title = title;
        this.credits = credits;
        this.teacher_id = teacher_id;
    }

    //Victoria
    public Course(String course_code, String title, int credits, int teacher_id) {
        this.course_code = course_code;
        this.title = title;
        this.credits = credits;
        this.teacher_id = teacher_id;
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
    public int getCourse_id() {
        return course_id;
    }

    public String getCourse_code() {
        return course_code;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    //Victoria
    //Setters (Change data. Put something in the box.)
    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

}

