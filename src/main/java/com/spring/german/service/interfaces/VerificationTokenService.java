package com.spring.german.service.interfaces;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public interface VerificationTokenService {

    Logger log = LoggerFactory.getLogger(VerificationTokenService.class);

    //TODO: correct place for it?
    static boolean isTokenExpired(VerificationToken verificationToken) {
        return LocalDate.now().isAfter(verificationToken.getExpiryDate());
    }

    void createVerificationToken(User user, String token);
}