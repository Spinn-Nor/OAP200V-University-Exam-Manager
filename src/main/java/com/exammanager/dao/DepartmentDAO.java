package com.exammanager.dao;

import com.exammanager.model.Department;
import com.exammanager.model.Teacher;
import com.exammanager.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * Implementation of the {@link DAO} interface for Department.
 * <p>
 * Provides database operations for the {@link Department} class, including CRUD (Create, Read, Update, Delete) functionality.
 * <p>
 * @author Bendik
 */
public class DepartmentDAO implements DAO<Department> {

    private final Connection conn;

    /**
     * Constructs a DepartmentDAO with the given database connection.
     *
     * @param conn the active database connection
     */

    public DepartmentDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves a department from the database by their ID.
     *
     * @param id the ID of the department to retrieve
     * @return {@link Optional} containing a {@link Department} object if successful, otherwise empty
     */
    @Override
    public Optional<Department> findById(int id) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get department. No database connection.");
            return Optional.empty();
        }

        String sql = "SELECT * FROM department WHERE id = ?";

        Optional<Department> department = Optional.empty();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                department = Optional.of(new Department(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while getting department: " + e.getMessage());
        }

        return department;
    }

    /**
     * Retrieves a department from the database by their name.
     *
     * @param name the name of the department to retrieve
     * @return {@link Optional} containing a {@link Department} object if successful, otherwise empty
     */
    public Optional<Department> findByName(String name) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get department. No database connection.");
            return Optional.empty();
        }

        String sql = "SELECT * FROM department WHERE name = ?";

        Optional<Department> department = Optional.empty();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                department = Optional.of(new Department(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while getting department: " + e.getMessage());
        }

        return department;
    }

    /**
     * Retrieves a list of all departments from the database.
     *
     * @return {@link ObservableList} of {@link Department} objects containing all departments
     * in the database if successful, otherwise an empty {@link ObservableList}
     */
    @Override
    public ObservableList<Department> findAll() {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get departments. No database connection.");
            return FXCollections.observableArrayList();
        }

        String sql = "SELECT * FROM department ORDER BY id";

        ObservableList<Department> departments = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Department department = new Department(rs.getInt("id"), rs.getString("name"));
                departments.add(department);
            }
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while getting departments: " + e.getMessage());
        }

        return departments;
    }

    /**
     * Inserts a new department into the database.
     *
     * @param department a {@link Department} object containing information about the department to be added
     */
    @Override
    public void addSingle(Department department) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to add department. No database connection.");
            return;
        }

        String sql = "INSERT INTO department (id, name) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, department.getId());
            stmt.setString(2, department.getName());
            stmt.executeUpdate();
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while adding department: " + e.getMessage());
        }
    }

    /**
     * Updates a department in the database.
     * <p>
     * Updates based on the department parameter's ID, setting values to those of the department parameter.
     *
     * @param department a {@link Department} object
     */
    @Override
    public void updateSingle(Department department) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to update department. No database connection.");
            return;
        }

        String sql  = "UPDATE department SET name = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, department.getName());
            stmt.setInt(2, department.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError("Error while updating department: " + e.getMessage());
        }
    }

    /**
     * Deletes a department in the database.
     *
     * @param departments {@link ObservableList} of department to delete
     */
    @Override
    public void deleteList(ObservableList<Department> departments) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to delete department(s). No database connection.");
            return;
        }

        String sql = "DELETE FROM department WHERE id = ?";

        for (Department department : departments) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Check if department has registered teachers
                PreparedStatement departmentEmptyStmt = conn.prepareStatement("SELECT * FROM teacher WHERE department = ?");
                departmentEmptyStmt.setString(1, department.getName());

                // Delete department if no teachers are registered to it
                // Otherwise display an error
                if (!departmentEmptyStmt.executeQuery().isBeforeFirst()) {
                    int id = department.getId();
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                } else {
                    AlertUtil.genericError("Error deleting department", "Cannot delete non-empty departments.\n"
                    + department.getName() + " currently has employed teachers.");
                }

            } catch (Exception e) {
                AlertUtil.showDatabaseConnectionError("Error while deleting department(s): " + e.getMessage());
            }
        }
    }
}
