package com.spring.german.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReadmeNotFoundException extends RuntimeException {

    public ReadmeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadmeNotFoundException(String message) {
        super(message);
    }
}
