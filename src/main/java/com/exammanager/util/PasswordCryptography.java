package com.exammanager.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * An abstract class containing static methods for password cryptography.
 * Contains a method for hashing passwords with a given salt and a method for
 * generating a random 8-character salt.
 * <p>
 * @author Bendik
 */
public abstract class PasswordCryptography {

    /**
     * A method for hashing passwords. Uses the PBKDF2 key derivation function
     * and SHA-256 hash function to hash passwords using a given salt and
     * an iteration count of 65536. Then encodes and returns the resulting byte array
     * to a base64 string.
     * <p>
     * @param password the password to hash
     * @param salt the salt used for 'salting'
     * @return the password hash encoded as a base64 string
     */
    public static String hashPassword(String password, String salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashBytes = skf.generateSecret(spec).getEncoded();

            String hash = Base64.getEncoder().encodeToString(hashBytes);

            return hash;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to hash password. " + e);
        }
    }

    /**
     * Generates a new, random, 8-character salt used for password hashing, consisting
     * of numbers, uppercase and lower case letters.
     * @return the generated salt
     */
    public static String generateSalt() {
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        return salt.toString();
    }

}
