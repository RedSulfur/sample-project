package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.interfaces.Creating;
import com.spring.german.service.interfaces.Searching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService implements Searching<VerificationToken>,
        Creating {

    private VerificationTokenRepository tokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getEntityByKey(String key) {
        return tokenRepository.findByToken(key);
    }
}