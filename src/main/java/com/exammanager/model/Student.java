package com.exammanager.model;

//Victoria
public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private int enrollmentYear;

    //empty constructor for database
    public Student(){
    }

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

    //Getters (Reach data and sees what is inside. It looks inside the box.)

    //Victoria
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

    //Victoria
    //Not done
    @Override
    public String toString() {
        return "";
    }

    public int getId() {
        return id;
    }
}
