package com.spring.german.exceptions;

public class EmptyRepositoryNameException extends RuntimeException {
    public EmptyRepositoryNameException(String message) {
        super(message);
    }
}
