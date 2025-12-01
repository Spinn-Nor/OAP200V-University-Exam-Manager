package com.exammanager.dao;

import com.exammanager.model.Course;
import com.exammanager.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementation of the {@link DAO} interface for Course.
 * <p>
 * Provides database operations for the {@link Course} class, including CRUD (Create, Read, Update, Delete) functionality.
 * <p>
 * @author Bendik
 */
public class CourseDAO implements DAO<Course> {

    private Connection conn;

    public CourseDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves a course from the database by its ID.
     *
     * @param id the ID of the course to retrieve
     * @return {@link Optional} containing a {@link Course} object if successful, otherwise empty
     */
    @Override
    public Optional<Course> findById(int id) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get course. No database connection.");
            return Optional.empty();
        }

        String sql = "SELECT * FROM course WHERE id = ?";

        Optional<Course> course = Optional.empty();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                course = Optional.of(new Course(
                        rs.getInt("id"),
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credits"),
                        rs.getInt("teacher_id")
                ));
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting teacher: " + e.getMessage());
        }

        return course;
    }

    /**
     * Retrieves a list of all courses from the database.
     *
     * @return {@link ObservableList} of {@link Course} objects containing all course
     * in the database if successful, otherwise an empty {@link ObservableList}
     */
    @Override
    public ObservableList<Course> findAll() {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get courses. No database connection.");
            return FXCollections.observableArrayList();
        }

        String sql = "SELECT * FROM course";

        ObservableList<Course> courses = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("id"),
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credits"),
                        rs.getInt("teacher_id")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting courses: " + e.getMessage());
        }

        return courses;
    }

    /**
     * Inserts a new course into the database.
     *
     * @param course a {@link Course} object containing information about the course to be added
     */
    @Override
    public void addSingle(Course course) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to add course. No database connection.");
            return;
        }

        String sql = "INSERT INTO course (course_code, title, credits, teacher_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getTitle());
            stmt.setInt(3, course.getCredits());
            stmt.setInt(4, course.getTeacherId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while adding course: " + e.getMessage());
        }
    }

    /**
     * Updates a course in the database.
     * <p>
     * Updates based on the course parameter's ID, setting values to those of the course parameter.
     *
     * @param course a {@link Course} object
     */
    @Override
    public void updateSingle(Course course) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to update course. No database connection.");
            return;
        }

        String sql = "UPDATE course SET course_code = ?, title = ?, credits = ?, teacher_id = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getTitle());
            stmt.setInt(3, course.getCredits());
            stmt.setInt(4, course.getTeacherId());
            stmt.setInt(5, course.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while updating course: " + e.getMessage());
        }
    }

    /**
     * Deletes a course in the database.
     *
     * @param courses {@link ObservableList} of courses to delete
     */
    @Override
    public void deleteList(ObservableList<Course> courses) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to delete course(s). No database connection.");
            return;
        }

        String sql = "DELETE FROM course WHERE id = ?";

        for (Course course : courses) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int id = course.getId();
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                AlertUtil.showDatabaseConnectionError("Error while deleting course(s): " + e.getMessage());
            }
        }
    }
}
