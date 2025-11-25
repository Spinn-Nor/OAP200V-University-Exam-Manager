package com.exammanager.login;

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