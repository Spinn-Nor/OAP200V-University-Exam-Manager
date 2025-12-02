package com.exammanager.model;

/**
 * A sealed class representing a person. Used as a parent class
 * for Teacher and Student to define shared attributes and methods.
 * Includes two constructors, one with ID for handling database returns,
 * and one without ID for adding new people, as well as getters and
 * setters for all attributes.
 * <p>
 * @author Bendik
 */
public sealed class Person permits Teacher, Student {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public Person(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
