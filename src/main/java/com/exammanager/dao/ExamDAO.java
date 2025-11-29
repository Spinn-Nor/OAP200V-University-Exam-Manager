package com.exammanager.dao;

import com.exammanager.model.Exam;
import com.exammanager.model.Teacher;
import com.exammanager.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Optional;

public class ExamDAO implements DAO<Exam> {
    private final Connection conn;

    /**
     * Constructs a TeacherDAO with the given database connection.
     *
     * @param conn the active database connection
     */
    public ExamDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves an exam from the database by its ID.
     *
     * @param id the ID of the teacher to retrieve
     * @return {@link Optional} containing a {@link Teacher} object if successful, otherwise empty
     */
    @Override
    public Optional<Exam> findById(int id) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get exam. No database connection.");
            return Optional.empty();
        }

        String sql = "SELECT * FROM exam WHERE id = ?";

        Optional<Exam> exam = Optional.empty();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exam = Optional.of(new Exam(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        // FIXME! Change to Date type
                        rs.getDate("exam_date").toLocalDate(),
                        rs.getString("grade")
                ));
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting exam: " + e.getMessage());
        }

        return exam;
    }

    /**
     * Retrieves a list of all exams associated with a student from the database.
     *
     * @param email the email associated with the student whose exams should be retrieved
     * @return {@link ObservableList} of {@link Exam} objects containing all exams
     * in the database associated with a specific student if successful,
     * otherwise an empty {@link ObservableList}
     */
    public ObservableList<Exam> findAllByEmail(String email) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get exams. No database connection.");
            return FXCollections.observableArrayList();
        }

        String sql = "SELECT e.id, e.student_id, e.course_id, e.exam_date, e.grade FROM exam AS e " +
                     "INNER JOIN student AS s ON e.student_id == s.id " +
                     "WHERE s.email = ?";

        ObservableList<Exam> exams = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(sql) ) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exam exam = new Exam(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getDate("exam_date").toLocalDate(),
                        rs.getString("grade")
                );
                exams.add(exam);
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting exams: " + e.getMessage());
        }

        return exams;
    }

    /**
     * Retrieves a list of all exams associated with a specific course from the database.
     *
     * @param courseId the ID of the course whose exams should be retrieved
     * @return {@link ObservableList} of {@link Exam} objects containing all exams
     * associated with a specific course student if successful,
     * or an empty {@link ObservableList} if none are found
     */
    public ObservableList<Exam> findByCourseId(int courseId) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get exams. No database connection.");
            return FXCollections.observableArrayList();
        }

        String sql = "SELECT * FROM exam WHERE course_id = ?";

        ObservableList<Exam> exams = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exam exam = new Exam(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getDate("exam_date").toLocalDate(),
                        rs.getString("grade")
                );
                exams.add(exam);
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting exams: " + e.getMessage());
        }

        return exams;
    }

    /**
     * Retrieves a list of all exams from the database.
     *
     * @return {@link ObservableList} of {@link Exam} objects containing all exams
     * in the database if successful, otherwise an empty {@link ObservableList}
     */
    @Override
    public ObservableList<Exam> findAll() {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to get exams. No database connection.");
            return FXCollections.observableArrayList();
        }

        String sql = "SELECT * FROM exam";

        ObservableList<Exam> exams = FXCollections.observableArrayList();

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Exam exam = new Exam(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getDate("exam_date").toLocalDate(),
                        rs.getString("grade")
                );
                exams.add(exam);
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while getting teachers: " + e.getMessage());
        }

        return exams;
    }

    /**
     * Inserts a new exam into the database.
     *
     * @param exam a {@link Exam} object containing information about the exam to be added
     */
    @Override
    public void addSingle(Exam exam) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to add exam. No database connection.");
            return;
        }

        String sql = "INSERT INTO exam (student_id, course_id, exam_date, grade) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exam.getStudentId());
            stmt.setInt(2, exam.getCourseId());
            stmt.setDate(3, Date.valueOf(exam.getExamDate()));
            stmt.setString(4, exam.getGrade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while adding exam: " + e.getMessage());
        }
    }

    /**
     * Updates an exam in the database.
     * <p>
     * Updates based on the exam parameter's ID, setting values to those of the exam parameter.
     *
     * @param exam a {@link Exam} object
     */
    @Override
    public void updateSingle(Exam exam) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to update exam. No database connection.");
            return;
        }

        String sql = "UPDATE exam SET student_id = ?, course_id = ?, exam_date = ?, grade = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exam.getStudentId());
            stmt.setInt(2, exam.getCourseId());
            stmt.setDate(3, Date.valueOf(exam.getExamDate()));
            stmt.setString(4, exam.getGrade());
            stmt.setInt(5, exam.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtil.showDatabaseConnectionError("Error while updating exam: " + e.getMessage());
        }
    }

    /**
     * Deletes an exam in the database.
     *
     * @param exams {@link ObservableList} of exams to delete
     */
    @Override
    public void deleteList(ObservableList<Exam> exams) {
        if (conn == null) {
            AlertUtil.showDatabaseConnectionError("Failed to delete teacher(s). No database connection.");
            return;
        }

        String sql = "DELETE FROM exam WHERE id = ?";

        for (Exam exam : exams) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int id = exam.getId();
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                AlertUtil.showDatabaseConnectionError("Error while deleting exam(s): " + e.getMessage());
            }
        }
    }
}
