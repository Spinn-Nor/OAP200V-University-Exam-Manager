package com.exammanager.dao;

import com.exammanager.model.Student;
import com.exammanager.model.Teacher;
import com.exammanager.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementation of the {@link DAO} interface for Student.
 * <p>
 * Provides database operations for the {@link Student} class, including CRUD (Create, Read, Update, Delete) functionality.
 * <p>
 * @author Bendik
 */
public class StudentDAO implements DAO<Student> {

    private final Connection conn;

    /**
     * Implementation of the {@link DAO} interface for Student.
     * <p>
     * Provides database operations for the {@link Student} class, including CRUD (Create, Read, Update, Delete) functionality.
     */
    public StudentDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves a student from the database by their ID.
     *
     * @param id the ID of the student to retrieve
     * @return {@link Optional} containing a {@link Student} object if successful, otherwise empty
     */
    @Override
    public Optional<Student> findById(int id) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get student. No database connection.");
            return Optional.empty();
        }

        String sql = "SELECT * FROM student WHERE id = ?";

        Optional<Student> student = Optional.empty();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                student = Optional.of(new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getInt("enrollment_year")
                ));
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting student: " + e.getMessage());
        }

        return student;
    }

    /**
     * Retrieves a student from the database by their email.
     *
     * @param email the email of the student to retrieve
     * @return {@link Optional} containing a {@link Student} object if successful, otherwise empty
     */
    public Optional<Student> findByEmail(String email) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get student. No database connection.");
            return Optional.empty();
        }

        String sql = "SELECT * FROM student WHERE email = ?";

        Optional<Student> student = Optional.empty();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                student = Optional.of(new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getInt("enrollment_year")
                ));
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting student: " + e.getMessage());
        }

        return student;
    }

    /**
     * Retrieves a list of all students from the database.
     *
     * @return {@link ObservableList} of {@link Student} objects containing all students
     * in the database if successful, otherwise an empty {@link ObservableList}
     */
    @Override
    public ObservableList<Student> findAll() {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get students. No database connection.");
        }

        String sql = "SELECT * FROM student";

        ObservableList<Student> students = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getInt("enrollment_year")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting students: " + e.getMessage());
        }

        return students;
    }

    /**
     * Inserts a new student into the database.
     *
     * @param student a {@link Student} object containing information about the student to be added
     */
    @Override
    public void addSingle(Student student) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to add student. No database connection.");
            return;
        }

        String sql = "INSERT INTO student (first_name, last_name, email, enrollment_year) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setInt(4, student.getEnrollmentYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while adding student: " + e.getMessage());
        }
    }

    /**
     * Updates a student in the database.
     * <p>
     * Updates based on the student parameter's ID, setting values to those of the student parameter.
     *
     * @param student a {@link Student} object
     */
    @Override
    public void updateSingle(Student student) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to update student. No database connection.");
            return;
        }

        String sql = "UPDATE student SET first_name = ?, last_name = ?, email = ?, enrollment_year = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setInt(4, student.getEnrollmentYear());
            stmt.setInt(5, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while updating student: " + e.getMessage());
        }
    }

    /**
     * Deletes a student in the database.
     *
     * @param students {@link ObservableList} of students to delete
     */
    @Override
    public void deleteList(ObservableList<Student> students) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to delete student(s). No database connection.");
            return;
        }

        String sql = "DELETE FROM student WHERE id = ?";

        for (Student student : students) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int id =  student.getId();
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } catch (SQLException e)  {
                AlertUtil.showDatabaseConnectionError("Error while deleting student(s): " + e.getMessage());
            }
        }
    }
}
