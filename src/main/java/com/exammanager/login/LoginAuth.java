package com.exammanager.login;

import com.exammanager.util.AlertUtil;
import com.exammanager.util.DatabaseConnection;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Optional;

public class LoginAuth {

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
        String sql = "SELECT hash, salt, accessLevel FROM users WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("hash");
                String salt = rs.getString("salt");
                // TODO! CHECK THIS
                AccessLevel accessLevel = AccessLevel.valueOf(rs.getString("accessLevel"));

                accessLevel.setEmail(email);

                UserDetails userDetails = new UserDetails(email, hash, salt, accessLevel);
                return Optional.of(userDetails);
            }
        } catch (Exception e) {
            AlertUtil.showDatabaseConnectionError(e.getMessage());
        }

        return Optional.empty();
    }

    private static boolean validatePassword(String password, String hash, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] passwordHash = skf.generateSecret(spec).getEncoded();

            if (Arrays.equals(passwordHash, hash.getBytes())) {
                return true;
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to validate password. " + e);
        }

        return false;
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
