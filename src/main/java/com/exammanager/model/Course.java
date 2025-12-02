package com.exammanager.model;

/**
 * Represents a course in the Exam Manager application.
 * <p>
 * This class is a plain data holder (POJO) containing basic
 * information about a course, including:
 * <ul>
 *     <li>ID</li>
 *     <li>Course code</li>
 *     <li>Title</li>
 *     <li>Credits</li>
 *     <li>ID of the teacher teaching the course</li>
 * </ul>
 * <p>
 * It provides getters all fields so the data can be
 * accessed as necessary by the application.
 * <p>
 * The course class has two constructors: one with ID for handling database returns, and
 * one without ID for adding new courses.
 *
 * @author Victoria
 */
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

