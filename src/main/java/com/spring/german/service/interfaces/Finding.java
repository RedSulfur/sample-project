package com.spring.german.service.interfaces;

public interface Finding<T> {
    T findById(long id);
}