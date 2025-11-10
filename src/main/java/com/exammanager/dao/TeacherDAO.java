package com.exammanager.dao;

import com.exammanager.model.Teacher;
import com.exammanager.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of the {@link DAO} interface for Teacher.
 * <p>
 * Provides database operations for the {@link Teacher} class, including CRUD (Create, Read, Update, Delete) functionality.
 */
public class TeacherDAO implements DAO<Teacher> {

    private final Connection conn;

    /**
     * Constructs a TeacherDAO with the given database connection.
     *
     * @param conn the active database connection
     */
    public TeacherDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves a teacher from the database by their ID.
     *
     * @param id the ID of the teacher to retrieve
     * @return {@link Optional} containing a {@link Teacher} object if successful, otherwise empty
     */
    @Override
    public Optional<Teacher> findById(int id) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get teacher. No database connection.");
            return Optional.empty();
        }

        String sql = "SELECT * FROM teacher WHERE id = ?";

        Optional<Teacher> teacher = Optional.empty();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                teacher = Optional.of(new Teacher(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("department"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting teacher: " + e.getMessage());
        }

        return teacher;
    }

    /**
     * Retrieves a list of all teachers from the database.
     *
     * @return {@link ObservableList} of {@link Teacher} objects containing all teachers
     * in the database if successful, otherwise an empty {@link ObservableList}
     */
    @Override
    public ObservableList<Teacher> findAll() {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get teachers. No database connection.");
            return FXCollections.observableArrayList();
        }

        String sql = "SELECT * FROM teacher";

        ObservableList<Teacher> teachers = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Teacher teacher = new Teacher(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("department"),
                        rs.getString("email")
                );
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting teachers: " + e.getMessage());
        }

        return teachers;
    }

    /**
     * Inserts a new teacher into the database.
     *
     * @param teacher a {@link Teacher} object containing information about the teacher to be added
     */
    @Override
    public void addSingle(Teacher teacher) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to add teacher. No database connection.");
            return;
        }

        String sql = "INSERT INTO teacher (firstName, lastName, department, email) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getDepartment());
            stmt.setString(4, teacher.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while adding teacher: " + e.getMessage());
        }
    }

    /**
     * Updates a teacher in the database.
     * <p>
     * Updates based on the teacher parameter's ID, setting values to those of the teacher parameter.
     *
     * @param teacher a {@link Teacher} object
     */
    @Override
    public void updateSingle(Teacher teacher) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to update teacher. No database connection.");
            return;
        }

        String sql = "UPDATE teacher SET firstName = ?, lastName = ?, department = ?, email = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getDepartment());
            stmt.setString(4, teacher.getEmail());
            stmt.setInt(5, teacher.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while updating teacher: " + e.getMessage());
        }
    }

    /**
     * Deletes a teacher in the database.
     *
     * @param teachers {@link ObservableList} of teachers to delete
     */
    @Override
    public void deleteList(ObservableList<Teacher> teachers) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to delete teacher. No database connection.");
        }

        String sql = "DELETE FROM teacher WHERE id = ?";

        for (Teacher teacher : teachers) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int id = teacher.getId();
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                AlertUtil.showDatabaseConnectionError("Error while deleting teacher: " + e.getMessage());
            }
        }
    }
}
