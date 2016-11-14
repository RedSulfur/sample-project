package com.spring.german.exceptions;

public class RepositoryNotSpecifiedException extends RuntimeException {
    public RepositoryNotSpecifiedException(String message) {
        super(message);
    }
}
