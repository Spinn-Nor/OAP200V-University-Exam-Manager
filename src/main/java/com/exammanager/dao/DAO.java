package com.exammanager.dao;

import javafx.collections.ObservableList;

import java.util.Optional;

// DAO (Data Access Object) interface for adding CRUD functionality
public interface DAO<T> {

    public Optional<T> findById(int id);

    public ObservableList<T> findAll();

    public void addSingle(T object);

    public void updateSingle(T object);

    public void deleteList(ObservableList<T> objects);
}
