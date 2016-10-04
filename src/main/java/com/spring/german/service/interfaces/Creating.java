package com.spring.german.service.interfaces;

import com.spring.german.entity.User;

public interface Creating {
    void createVerificationToken(User user, String token);
}