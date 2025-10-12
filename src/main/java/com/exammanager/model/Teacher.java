package com.exammanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Teacher {
    private int id;
    private String firstName;
    private String lastName;
    private String department;
    private String email;

    // Constructor with all fields
    public Teacher(int id, String firstName, String lastName, String department, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.email = email;
    }

    // Constructor without ID
    public Teacher(String firstName, String lastName, String department, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.email = email;
    }

    // Generate ObservableList of example teachers
    public static ObservableList<Teacher> generateExampleTeachers() {
        ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

        Teacher teacher1 = new Teacher(1, "John", "Doe", "Mathematics", "johndoe@school.com");
        Teacher teacher2 = new Teacher(2, "Jane", "Smith", "Physics", "janesmith@school.com");
        Teacher teacher3 = new Teacher(3, "Michael", "Johnson", "History", "michaeljohnson@school.com");
        Teacher teacher4 = new Teacher(4, "Emily", "Davis", "English", "emilydavis@school.com");
        Teacher teacher5 = new Teacher(5, "David", "Martinez", "Computer Science", "davidmartinez@school.com");

        teacherList.addAll(teacher1, teacher2, teacher3, teacher4, teacher5);

        return teacherList;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
