package com.exammanager.dao;

import javafx.collections.ObservableList;

import java.util.Optional;

// DAO (Data Access Object) interface for adding CRUD functionality

/**
 * Data Access Object (DAO) interface.
 * <p>
 * Defines methods for basic CRUD (Create, Read, Update, Delete) operations.
 * Takes a generic parameter, which allows for implementation on various DAO-classes.
 *
 * @param <T> a generic parameter. Can be any DAO-class.
 *
 * @author Bendik
 */
public interface DAO<T> {

    public Optional<T> findById(int id);

    public ObservableList<T> findAll();

    public void addSingle(T object);

    public void updateSingle(T object);

    public void deleteList(ObservableList<T> objects);
}
