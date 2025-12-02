package com.exammanager.login;

/**
 * An enum defining the different access levels of the application.
 * Used to define to level of access the currently logged-in user should have.
 * <p>
 * @author Bendik
 */
public enum AccessLevel {
    ADMIN(""),
    TEACHER(""),
    STUDENT("");

    private String email;

    AccessLevel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}