package com.exammanager.login;

import com.exammanager.util.AlertUtil;
import com.exammanager.util.DatabaseConnection;
import com.exammanager.util.PasswordCryptography;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

/**
 * A class for handling login. Contains a public method loginAuth used
 * to check login credentials, two private helper methods for validating
 * emails and passwords, and a private static class for holding account details.
 * <p>
 * @author Bendik
 */
public class LoginAuth {

    /**
     * Handles login. Takes an email and a password as arguments. First
     * checks if the email is associated with an existing user account. If
     * the email is correct, it then hashes the password and matches it to the
     * stored hash associated with the account.
     * <p>
     * @param email the email address of the user trying to log in
     * @param password the password of the user trying to log in
     * @return a {@link Optional} containing the access level of the account if the
     * email and passwords are correct, otherwise returns an empty {@link Optional}
     */
    public static Optional<AccessLevel> loginAuth(String email, String password) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            return Optional.empty();
        }

        Optional<UserDetails> userDetails = validateUser(conn, email);

        if (userDetails.isPresent()) {
            String hash = userDetails.get().hash;
            String salt = userDetails.get().salt;

            if (validatePassword(password, hash, salt)) {
                return Optional.of(userDetails.get().accessLevel);
            }
        }
        // TODO! TEST THIS
        AlertUtil.genericError("Login failed.", "Incorrect email or password.");
        return Optional.empty();
    }

    private static Optional<UserDetails> validateUser(Connection conn, String email) {
        String sql = "SELECT hash, salt, access_level FROM user WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("hash");
                String salt = rs.getString("salt");
                // TODO! CHECK THIS
                AccessLevel accessLevel = AccessLevel.valueOf(rs.getString("access_level"));

                accessLevel.setEmail(email);

                UserDetails userDetails = new UserDetails(email, hash, salt, accessLevel);
                return Optional.of(userDetails);
            }
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError(e.getMessage());
        }

        return Optional.empty();
    }

    private static boolean validatePassword(String password, String databaseHash, String salt) {
        String passwordHashString = PasswordCryptography.hashPassword(password, salt);

        byte[] hashBytes = Base64.getDecoder().decode(passwordHashString);
        byte[] databaseHashBytes = Base64.getDecoder().decode(databaseHash);

        return Arrays.equals(hashBytes, databaseHashBytes);
    }

    private static class UserDetails {
        String email;
        String hash;
        String salt;
        AccessLevel accessLevel;

        public UserDetails(String email, String hash, String salt, AccessLevel accessLevel) {
            this.email = email;
            this.hash = hash;
            this.salt = salt;
            this.accessLevel = accessLevel;
        }
    }

}
