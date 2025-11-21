package com.exammanager.model;


    public class Exam {
        private int id;
        private int studentId;
        private int courseId;
        private String examDate;
        private String grade;

        //empty constructor for database
        public Exam(){
        }

        public Exam(int id, int studentId, int courseId, String examDate, String grade) {
            this.id = id;
            this.studentId = studentId;
            this.courseId = courseId;
            this.examDate = examDate;
            this.grade = grade;
        }

        public Exam(int StudentId, int courseId, String examDate, String grade) {
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

        public String getExamDate() {
            return examDate;
        }

        public void setExamDate(String examDate) {
            this.examDate = examDate;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
    }

