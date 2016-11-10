package com.spring.german.service.interfaces;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public interface VerificationTokenService {

    //TODO: correct place for it?
    static boolean isTokenExpired(VerificationToken verificationToken) {
        System.out.println("Time fetched from verification token: " + verificationToken.getExpiryDate().getTime());
        System.out.println("Current time: " + Calendar.getInstance().getTime().getTime());
        return (verificationToken.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime()) <= 0;
    }

    void createVerificationToken(User user, String token);
}