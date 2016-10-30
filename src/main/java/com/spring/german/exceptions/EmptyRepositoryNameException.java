package com.spring.german.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmptyRepositoryNameException extends RuntimeException {
    public EmptyRepositoryNameException(String message) {
        super(message);
    }
}
