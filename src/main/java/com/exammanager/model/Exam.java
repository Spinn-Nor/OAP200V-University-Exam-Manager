package com.exammanager.model;

import java.time.LocalDate;

/**
 * Represents an exam in the Exam Manager application.
 * <p>
 * This class is a plain data holder (POJO) containing basic
 * information about an exam, including:
 * <ul>
 *     <li>ID</li>
 *     <li>ID of the student who took the exam</li>
 *     <li>ID of the course the exam is for</li>
 *     <li>Date of the exam</li>
 *     <li>Grade of the exam if applicable</li>
 * </ul>
 * <p>
 * It provides getters all fields so the data can be
 * accessed as necessary by the application.
 * <p>
 * The course class has two constructors: one with ID for handling database returns, and
 * one without ID for adding new exams.
 *
 * @author Synnøve
 */
public class Exam {
        private int id;
        private int studentId;
        private int courseId;
        private LocalDate examDate;
        private String grade;

        public Exam(int id, int studentId, int courseId, LocalDate examDate, String grade) {
            this.id = id;
            this.studentId = studentId;
            this.courseId = courseId;
            this.examDate = examDate;
            this.grade = grade;
        }

        public Exam(int studentId, int courseId, LocalDate examDate, String grade) {
            this.studentId = studentId;
            this.courseId = courseId;
            this.examDate = examDate;
            this.grade = grade;
        }

        //Getters (Reach data and sees what is inside. It looks inside the box.)


        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public LocalDate getExamDate() {
            return examDate;
        }

        public void setExamDate(LocalDate examDate) {
            this.examDate = examDate;
        }

        public String getGrade() {
            if (grade != null) {
                return grade;
            } else  {
                return "No grade";
            }
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
    }

