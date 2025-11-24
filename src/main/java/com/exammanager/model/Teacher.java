package com.exammanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
 * It provides, or inherits, getters and setters for all fields so the data can be
 * accessed and modified as necessary by the application.
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
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
