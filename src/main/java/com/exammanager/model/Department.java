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

    // FIXME! FOR TESTING
    public static ObservableList<Department>generateExampleDepartments() {
        ObservableList<Department> departments = FXCollections.observableArrayList();

        Department department1 = new Department(1, "History");
        Department department2 = new Department(2, "Computer Science");
        Department department3 = new Department(3, "Mathematics");
        Department department4 = new Department(4, "Physics");
        Department department5 = new Department(5, "Science");

        departments.addAll(department1, department2, department3, department4, department5);

        return departments;
    }

    @Override
    public String toString() {
        return name;
    }
}