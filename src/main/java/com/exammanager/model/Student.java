package com.exammanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a student in the Exam Manager application.
 * <p>
 * This class extends the {@link Person} class. This class is a plain data holder (POJO)
 * containing basic information about a student, including:
 * <ul>
 *     <li>ID</li>
 *     <li>First name</li>
 *     <li>Last name</li>
 *     <li>Email address</li>
 *     <li>Enrollment year</li>
 * </ul>
 * <p>
 * It provides,or inherits, getters and setters for all fields so the data can be
 * accessed and modified as necessary by the application.
 * <p>
 * The student class has two constructors: one with ID for handling database returns, and
 * one without ID for adding new students.
 *
 * @author Victoria
 */
public final class Student extends Person {
    private int enrollmentYear;

    //Victoria
    public Student(int id, String firstName, String lastName, String email, int enrollmentYear) {
        super(id, firstName, lastName, email);
        this.enrollmentYear = enrollmentYear;
    }

    //Victoria
    public Student(String firstName, String lastName, String email, int enrollmentYear) {
        super(firstName, lastName, email);
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
    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    //Victoria
    //Setters (Change data. Put something in the box.)
    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

}
