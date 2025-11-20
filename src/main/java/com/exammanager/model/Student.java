package com.exammanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Victoria
public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private int enrollmentYear;

    //Victoria
    public Student(int id, String firstName, String lastName, String email, int enrollmentYear) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email =email;
        this.enrollmentYear = enrollmentYear;
    }

    //Victoria
    public Student(String firstName, String lastName, String email, int enrollmentYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enrollmentYear = enrollmentYear;
    }

    //Generate examples of Students
    public static  ObservableList<Student> generateExampleStudents() {
        ObservableList<Student> studentList = FXCollections.observableArrayList();

        Student student1 = new Student(1, "Victoria", "Stefanov", "skjerabagera@school.com", 2);
        Student student2 = new Student(2, "Synnøve", "Fevang", "hallaien@school.com", 2);
        Student student3 = new Student(3, "Ola", "Normann", "heipådeg@school.com", 1);
        Student student4 = new Student(4, "Katrine", "Hansen", "katr41@school.com", 3);
        Student student5 = new Student(5, "Dimitar", "Petrov", "Petrov1999@school.com", 1);

        studentList.addAll(student1, student2, student3, student4, student5);

        return studentList;
    }

    //Getters (Reach data and sees what is inside. It looks inside the box.)

    //Victoria
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    //Victoria
    //Setters (Change data. Put something in the box.)
    public void setID(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

}
