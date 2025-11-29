package com.exammanager.model;

import java.time.LocalDate;

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

        public Exam(int StudentId, int courseId, LocalDate examDate, String grade) {
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

