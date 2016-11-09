package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.exceptions.TokenNotFoundException;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.service.interfaces.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Optional.of;

@Service
public class DefaultVerificationTokenService implements Searching<VerificationToken>,
        VerificationTokenService {

    private VerificationTokenRepository tokenRepository;

    @Autowired
    public DefaultVerificationTokenService(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void createVerificationToken(User user, String token) {
        com.spring.german.entity.VerificationToken myToken = new com.spring.german.entity.VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getEntityByKey(String key) {
        return of(tokenRepository.findByToken(key))
                .orElseThrow(() -> new TokenNotFoundException("User not Found"));
    }
}