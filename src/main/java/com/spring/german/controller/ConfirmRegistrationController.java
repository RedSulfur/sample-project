package com.spring.german.controller;

import com.spring.german.entity.State;
import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.service.UserService;
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

    private UserService userService;

    @Autowired
    public ConfirmRegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * User reaches an endpoint corresponding to this method by following
     * a confirmation link in the email message. Method extracts a string
     * that represents a verification token name from a request parameter.
     * Checks whether an expiration time of the given token has not passed,
     * if it has passed, redirects user to a page that clearly describes an
     * occurred problem. If verification token is valid, specifies a page
     * where user can start searching for projects.
     *
     * @param request   an object that is used to determine a user's locale
     * @param tokenName name of the verification token
     * @return          {@link ModelAndView} object that contains no model
     *                  attributes and a default view name.
     */
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public ModelAndView confirmRegistration (WebRequest request,
                                       @RequestParam("token") String tokenName) {

        Locale locale = request.getLocale();
        
        VerificationToken verificationToken = userService.getVerificationToken(tokenName);

        ConfirmRegistrationControllerLogger.logVerificationTokenExpirationDate(verificationToken);
        
        if (verificationToken == null) {
            return getErrorModelAndView(locale);
        }

        User user = verificationToken.getUser();
        Calendar instance = Calendar.getInstance();

        if((verificationToken.getExpiryDate().getTime() - instance.getTime().getTime())<= 0) {
            return getErrorModelAndView(locale);
        }

        user.setState(State.ACTIVE.getState());
        userService.updateUser(user);

        return this.getDefaultModelAndView();
    }

    /**
     * Creates a default {@link ModelAndView} object
     *
     * @return default {@link ModelAndView}
     */
    private ModelAndView getDefaultModelAndView() {
        return new ModelAndView("gallery");
    }

    /**
     * Creates an error {@link ModelAndView} object
     *
     * @return error {@link ModelAndView}
     */
    private ModelAndView getErrorModelAndView(Locale locale) {
        ModelAndView modelAndView = new ModelAndView("gallery");
        modelAndView.addObject("lang", locale);
        return modelAndView;
    }

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
