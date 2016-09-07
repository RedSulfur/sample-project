package com.spring.german.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNameNotFoundException extends RuntimeException {

    private String name;

    public UserNameNotFoundException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
