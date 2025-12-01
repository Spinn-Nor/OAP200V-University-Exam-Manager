package com. exammanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Anwar
 */
public class Department {

    private int id;
    private String name;

    // Konstruktør med ID (brukes når department leses fra databasen)
    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Konstruktør uten ID (brukes når en ny department opprettes før lagring)
    public Department(String name) {
        this.name = name;
    }

    // Getters og setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}