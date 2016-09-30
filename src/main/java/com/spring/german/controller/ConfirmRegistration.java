package com.spring.german.controller;

import com.spring.german.entity.State;
import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Locale;

@Controller
public class ConfirmRegistration {

    Logger log = LoggerFactory.getLogger(ConfirmRegistration.class);
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration (WebRequest request, Model model,
                                       @RequestParam("token") String token) {

        Locale locale = request.getLocale();
        
        VerificationToken verificationToken = userService.getVerificationToken(token);

        log.info("Verification token was extracted from the database, its expire date is: {}",
                verificationToken.getExpiryDate());
        
        if (verificationToken == null) {
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar instance = Calendar.getInstance();

        if((verificationToken.getExpiryDate().getTime() - instance.getTime().getTime())<= 0) {
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        user.setState(State.ACTIVE.getState());
        userService.updateUser(user);

        return "gallery";
    }

}
