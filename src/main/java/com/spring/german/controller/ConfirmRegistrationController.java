package com.spring.german.controller;

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

import java.util.Locale;

import static com.spring.german.service.interfaces.VerificationTokenService.isTokenExpired;
import static com.spring.german.util.Endpoints.GALLERY_PAGE;
import static com.spring.german.util.Endpoints.TOKEN_EXPIRED_ERROR_PAGE;

@Controller
public class ConfirmRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(ConfirmRegistrationController.class);

    private Searching<VerificationToken> verificationTokenService;
    private UserService userService;

    @Autowired
    public ConfirmRegistrationController(Searching<VerificationToken> verificationTokenService,
                                         UserService userService) {
        this.verificationTokenService = verificationTokenService;
        this.userService = userService;
    }

    /**
     * Checks whether an expiration time of the {@code VerificationTokenService}
     * object has not passed and defines a view depending on the result of this check
     *
     * @param request   an object that is used to determine a user's locale
     * @param tokenName name of {@code VerificationTokenService} which expiration
     *                  time is being verified
     */
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public ModelAndView confirmRegistration (WebRequest request,
                                       @RequestParam("token") String tokenName) {
        Locale locale = request.getLocale();
        log.info("Locale: {}", locale);

        VerificationToken verificationToken = verificationTokenService.getEntityByKey(tokenName);
        log.info("verificationToken : {}", verificationToken);

        if(isTokenExpired(verificationToken)) {
            return this.getErrorModelAndView();   //error page is not ready yet
        } else {
            userService.updateUserState(verificationToken.getUser());
            return this.getDefaultModelAndView();
        }
    }

    private ModelAndView getDefaultModelAndView() {
        return new ModelAndView(GALLERY_PAGE);
    }

    private ModelAndView getErrorModelAndView() {
        return new ModelAndView(TOKEN_EXPIRED_ERROR_PAGE);
    }

    //TODO: create an error getErrorModelAndView method
}
