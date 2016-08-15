package com.spring.german.service;

import com.spring.german.entity.User;

public interface UserService {

    User save(User u);
    User findById(long id);
    User findBySso(String sso);

}
