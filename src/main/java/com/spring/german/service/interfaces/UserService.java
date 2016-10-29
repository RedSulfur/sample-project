package com.spring.german.service.interfaces;

import com.spring.german.entity.User;

public interface UserService {
    User save(User user);
    User findByEmail(String email);
    User updateUserState(User user);
}
