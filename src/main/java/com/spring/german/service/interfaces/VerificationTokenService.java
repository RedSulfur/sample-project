package com.spring.german.service.interfaces;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;

import java.time.LocalDate;

public interface VerificationTokenService {

    //TODO: correct place for it?
    static boolean isTokenExpired(VerificationToken verificationToken) {
        return (verificationToken.getExpiryDate().isAfter(LocalDate.now()));
    }

    void createVerificationToken(User user, String token);
}