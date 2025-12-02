package com.exammanager.model;

/**
 * Represents a teacher in the Exam Manager application.
 * <p>
 * This class extends the {@link Person} class. This class is a plain data holder (POJO)
 * containing basic information about a teacher, including:
 * <ul>
 *     <li>ID</li>
 *     <li>First name</li>
 *     <li>Last name</li>
 *     <li>Department</li>
 *     <li>Email address</li>
 * </ul>
 * <p>
 * It provides, or inherits, getters all fields necessary fields so the data can be
 * accessed as necessary by the application.
 * <p>
 * The teacher class has two constructors: one with ID for handling database returns, and
 * one without ID for adding new teachers.
 *
 * @author Bendik
 */
public final class Teacher extends Person {
    private String department;

    // Constructor with all fields
    public Teacher(int id, String firstName, String lastName, String department, String email) {
        super(id, firstName, lastName, email);
        this.department = department;
    }

    // Constructor without ID
    public Teacher(String firstName, String lastName, String department, String email) {
        super(firstName, lastName, email);
        this.department = department;
    }

    // Getters and setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return getId() + " - " + getFirstName() + " " + getLastName();
    }

}
