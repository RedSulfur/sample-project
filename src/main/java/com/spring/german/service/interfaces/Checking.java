package com.spring.german.service.interfaces;

public interface Checking<T> {
    boolean isPresent(T valueToCheck);
}
