package com.spring.german.controller;

import com.spring.german.entity.State;
import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Locale;

@Controller
public class ConfirmRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(ConfirmRegistrationController.class);

    public static final String GALLERY_PAGE = "gallery";

    private Searching<VerificationToken> tokenSearching;
    private UserService userService;

    @Autowired
    public ConfirmRegistrationController(Searching<VerificationToken> tokenSearching,
                                         UserService userService) {
        this.tokenSearching = tokenSearching;
        this.userService = userService;
    }

    /**
     * Method checks whether an expiration time of the {@code VerificationToken}
     * object has not passed.
     *
     * @param request   an object that is used to determine a user's locale
     * @param tokenName name of {@code VerificationToken} which expiration
     *                  time is being verified
     */
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public ModelAndView confirmRegistration (WebRequest request,
                                       @RequestParam("token") String tokenName) {

        Locale locale = request.getLocale();
        log.info("Locale: {}", locale);
        VerificationToken verificationToken = tokenSearching.getEntityByKey(tokenName);

        //TODO: verificationToken has nice toString()
        ConfirmRegistrationControllerLogger.logVerificationTokenExpirationDate(verificationToken);

        log.info("confirmRegistration#verificationToken: {}", verificationToken);
        if (verificationToken == null) {
            return this.getDefaultModelAndView();
        }

        User user = verificationToken.getUser();
        Calendar instance = Calendar.getInstance();

        if((verificationToken.getExpiryDate().getTime() - instance.getTime().getTime()) <= 0) {
            return this.getDefaultModelAndView();
        }

        user.setState(State.ACTIVE.getState());
        userService.updateUser(user);

        return this.getDefaultModelAndView();
    }

    /**
     * Creates a default {@link ModelAndView} object
     */
    private ModelAndView getDefaultModelAndView() {
        return new ModelAndView(GALLERY_PAGE);
    }

    //TODO: create an error getErrorModelAndView method

    /**
     * Provides helper methods for its outer class
     */
    private static class ConfirmRegistrationControllerLogger {

        private static final Logger log = LoggerFactory.getLogger(ConfirmRegistrationController.class);

        private static void logVerificationTokenExpirationDate(VerificationToken verificationToken) {
            log.info("Verification token was extracted from the database, its expire date is: {}",
                    verificationToken.getExpiryDate());
        }
    }
}
