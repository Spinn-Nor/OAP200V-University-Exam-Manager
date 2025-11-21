package com.exammanager.model;

public class Department {

    private int id;
    private String name;

    // Konstruktør med ID (brukes når department leses fra databasen)
    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Konstruktør uten ID (brukes når en ny department opprettes før lagring i databasen)
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

    // Nyttig slik at objekter vises riktig i ComboBox/TableView
    @Override
    public String toString() {
        return name;
    }
}
