package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;

public interface UserService {

    User save(User u);
    User findById(long id);
    User findBySso(String sso);
    User findByEmail(String email);
    void updateUser(User user);

    User getUser(String verificationToken);
    void createVerificationToken(User user, String token);
    VerificationToken getVerificationToken(String VerificationToken);
}
