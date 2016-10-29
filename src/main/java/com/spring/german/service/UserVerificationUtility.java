package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.interfaces.Searching;
import org.springframework.beans.factory.annotation.Autowired;

public class UserVerificationUtility implements Searching<User> {

    private VerificationTokenRepository tokenRepository;

    @Autowired
    public UserVerificationUtility(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User getEntityByKey(String key) {
        return tokenRepository.findByToken(key).getUser();
    }
}