package com.spring.german.exceptions;

public class ReadmeNotFound extends RuntimeException {

    public ReadmeNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadmeNotFound(String message) {
        super(message);
    }
}
