package com.spring.german.exceptions;

public class TechnologiesNotFoundException extends RuntimeException {
    public TechnologiesNotFoundException(String message) {
        super(message);
    }
}
