package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.interfaces.Searching;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserVerificationUtil implements Searching<User> {

    private VerificationTokenRepository tokenRepository;

    @Autowired
    public UserVerificationUtil(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<User> getEntityByKey(String key) {
        return Optional.of(tokenRepository.findByToken(key).getUser());
    }
}