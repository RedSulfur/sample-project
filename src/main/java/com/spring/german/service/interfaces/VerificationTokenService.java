package com.spring.german.service.interfaces;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;

import java.util.Calendar;

public interface VerificationTokenService {

    //TODO: correct place for it?
    static boolean isTokenExpired(VerificationToken verificationToken) {
        return verificationToken.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime() >= 0;
    }

    void createVerificationToken(User user, String token);
}