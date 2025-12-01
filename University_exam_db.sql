-- ============================================
-- University Exam Manager Database Setup Script
-- ============================================

-- 1. Create user "student" (if not exists) with password "student"
CREATE USER IF NOT EXISTS 'student'@'localhost' IDENTIFIED BY 'student';

-- 2. Drop and create the database
DROP DATABASE IF EXISTS university_exam_db;
CREATE DATABASE university_exam_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. Grant all privileges on this database to "student"
GRANT ALL PRIVILEGES ON university_exam_db.* TO 'student'@'localhost';
FLUSH PRIVILEGES;

-- 4. Use the new database
USE university_exam_db;

-- ============================================
-- Tables
-- ============================================

-- Table: student
DROP TABLE IF EXISTS student;
CREATE TABLE student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(150) UNIQUE NOT NULL,
    enrollment_year INT NOT NULL
);

-- Table: department
DROP TABLE IF EXISTS department;
CREATE TABLE department (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Table: teacher
DROP TABLE IF EXISTS teacher;
CREATE TABLE teacher (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    email      VARCHAR(150) UNIQUE NOT NULL,
    FOREIGN KEY (department) REFERENCES department(name)
);

-- Table: course
DROP TABLE IF EXISTS course;
CREATE TABLE course (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(150) NOT NULL,
    credits INT NOT NULL,
    teacher_id INT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id)
);

-- Table: exam
DROP TABLE IF EXISTS exam;
CREATE TABLE exam (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id  INT NOT NULL,
    exam_date  DATE NOT NULL,
    grade ENUM('A','B','C','D','E','F', 'No grade') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);

-- Table: user
DROP TABLE IF EXISTS user;
CREATE TABLE user (
    email VARCHAR(100) PRIMARY KEY,
    hash VARCHAR(255) NOT NULL,
    salt VARCHAR(100) NOT NULL,
    access_level ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL
);

-- ============================================
-- Sample Data
-- ============================================

-- Insert departments
INSERT INTO department (name) VALUES
('Computer Science'),
('Mathematics'),
('Physics'),
('Chemistry'),
('Biology'),
('Psychology'),
('Economics'),
('History'),
('Philosophy'),
('Mechanical Engineering');

-- Insert teachers
INSERT INTO teacher (first_name, last_name, department, email) VALUES
('Derek', 'Adams', 'Computer Science', 'derek.adams@university.edu'),
('Emily', 'Clark', 'Mathematics', 'emily.clark@university.edu'),
('Frank', 'Nelson', 'Physics', 'frank.nelson@university.edu'),
('Grace', 'Lopez', 'Chemistry', 'grace.lopez@university.edu'),
('Henry', 'Young', 'Biology', 'henry.young@university.edu'),
('Isabella', 'Green', 'Psychology', 'isabella.green@university.edu'),
('Jacob', 'Hall', 'Economics', 'jacob.hall@university.edu'),
('Kara', 'Allen', 'History', 'kara.allen@university.edu'),
('Liam', 'King', 'Philosophy', 'liam.king@university.edu'),
('Mia', 'Wright', 'Mechanical Engineering', 'mia.wright@university.edu'),
('Noah', 'Baker', 'Computer Science', 'noah.baker@university.edu'),
('Olivia', 'Turner', 'Mathematics', 'olivia.turner@university.edu'),
('Peter', 'Campbell', 'Physics', 'peter.campbell@university.edu'),
('Quinn', 'Mitchell', 'Chemistry', 'quinn.mitchell@university.edu'),
('Rachel', 'Evans', 'Biology', 'rachel.evans@university.edu'),
('Samuel', 'Rivera', 'Psychology', 'samuel.rivera@university.edu'),
('Tara', 'Ward', 'Economics', 'tara.ward@university.edu'),
('Uriel', 'Diaz', 'History', 'uriel.diaz@university.edu'),
('Vanessa', 'Snyder', 'Philosophy', 'vanessa.snyder@university.edu'),
('William', 'Fox', 'Mechanical Engineering', 'william.fox@university.edu');

-- Insert students
INSERT INTO student (first_name, last_name, email, enrollment_year) VALUES
('Alice', 'Reynolds', 'alice.reynolds@student.edu', 2021),
('Brian', 'Carter', 'brian.carter@student.edu', 2022),
('Chloe', 'Mitchell', 'chloe.mitchell@student.edu', 2023),
('Daniel', 'Scott', 'daniel.scott@student.edu', 2020),
('Ella', 'Bennett', 'ella.bennett@student.edu', 2024),
('Felix', 'Hughes', 'felix.hughes@student.edu', 2022),
('Hannah', 'Ward', 'hannah.ward@student.edu', 2021),
('Ian', 'Murphy', 'ian.murphy@student.edu', 2023),
('Julia', 'Foster', 'julia.foster@student.edu', 2024),
('Kevin', 'Brooks', 'kevin.brooks@student.edu', 2022),
('Lily', 'Rogers', 'lily.rogers@student.edu', 2021),
('Marcus', 'Parker', 'marcus.parker@student.edu', 2023),
('Nina', 'Barnes', 'nina.barnes@student.edu', 2020),
('Oscar', 'Coleman', 'oscar.coleman@student.edu', 2022),
('Paula', 'Sullivan', 'paula.sullivan@student.edu', 2024),
('Quentin', 'Chapman', 'quentin.chapman@student.edu', 2021),
('Rachel', 'Hayes', 'rachel.hayes@student.edu', 2023),
('Samuel', 'Jenkins', 'samuel.jenkins@student.edu', 2022),
('Tina', 'Stevens', 'tina.stevens@student.edu', 2024),
('Umar', 'Wheeler', 'umar.wheeler@student.edu', 2021),
('Vera', 'Holmes', 'vera.holmes@student.edu', 2023),
('Walter', 'Dixon', 'walter.dixon@student.edu', 2022),
('Xenia', 'Griffin', 'xenia.griffin@student.edu', 2024),
('Yusuf', 'Owens', 'yusuf.owens@student.edu', 2020),
('Zara', 'Payne', 'zara.payne@student.edu', 2021),
('Adrian', 'Sharp', 'adrian.sharp@student.edu', 2023),
('Bella', 'Rhodes', 'bella.rhodes@student.edu', 2022),
('Caleb', 'Fowler', 'caleb.fowler@student.edu', 2024),
('Diana', 'Harper', 'diana.harper@student.edu', 2021),
('Ethan', 'Lowe', 'ethan.lowe@student.edu', 2023);


-- Insert courses
INSERT INTO course (course_code, title, credits, teacher_id) VALUES
('CS201', 'Data Structures', 10, 4),
('MATH210', 'Calculus II', 10, 5),
('PHYS210', 'Classical Mechanics', 10, 6),
('CHEM110', 'Organic Chemistry I', 10, 7),
('BIO150', 'Cell Biology', 10, 8),
('PSY200', 'Cognitive Psychology', 10, 9),
('ECON220', 'Microeconomics', 10, 10),
('HIST130', 'World History', 10, 11),
('PHIL120', 'Intro to Ethics', 10, 12),
('MECH250', 'Thermodynamics', 10, 13);

-- Insert exams
INSERT INTO exam (student_id, course_id, exam_date, grade) VALUES
(5, 1, '2024-05-20', 'A'),
(6, 1, '2024-05-20', 'C'),
(7, 2, '2024-05-22', 'B'),
(8, 2, '2024-05-22', 'A'),
(9, 3, '2024-05-25', 'D'),
(10, 3, '2024-05-25', 'B'),
(11, 4, '2024-05-28', 'C'),
(12, 4, '2024-05-28', 'A'),
(13, 5, '2024-06-02', 'B'),
(14, 5, '2024-06-02', 'A'),
(15, 6, '2024-06-05', 'F'),
(16, 6, '2024-06-05', 'C'),
(17, 7, '2024-06-08', 'B'),
(18, 7, '2024-06-08', 'A'),
(19, 8, '2024-06-12', 'D'),
(20, 8, '2024-06-12', 'C'),
(21, 9, '2024-06-15', 'A'),
(22, 9, '2024-06-15', 'B'),
(23, 10, '2024-06-18', 'C'),
(24, 10, '2024-06-18', 'A');

-- Insert users
INSERT INTO user (email, hash, salt, access_level) VALUES
-- hash = admin
('admin@email.com', 'a8xMBu3kjq2yIt+BoxA9wdSFWHCAxXa/bFxbnhv3mDw=', 'aH8K2ou', 'ADMIN'),
-- hash = teacher
('teacher@email.com', 'FxT7tXQ78bi6NZ8xttO3ztNEhK9ANmPAU+ht+xiL+4g=', 'j2ABW8aR', 'TEACHER'),
-- hash = student
('student@email.com', 'IXRbyWsSzzkVNkDS2wnDZ/a2sHKGtPO56AdpUNaEnMk=', 'uJA24Ka2', 'STUDENT');