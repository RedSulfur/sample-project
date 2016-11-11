package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.registration.ApplicationDetails;
import com.spring.german.registration.OnRegistrationCompleteEvent;
import com.spring.german.service.interfaces.UserService;
import com.spring.german.validation.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

import static com.spring.german.util.Endpoints.LOGIN_PAGE;
import static com.spring.german.util.Endpoints.REGISTRATION_PAGE;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    private UserService userService;
    private UserValidator validator;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(UserService userService,
                                  UserValidator validator,
                                  ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.validator = validator;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        return this.getLoginModelAndView();
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView model) {

        model.setViewName(REGISTRATION_PAGE);
        model.addObject(new User());
        return model;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute(value = "user")
                                   @Validated @Valid User user,
                               BindingResult result,
                               HttpServletRequest request) {

        validator.validate(user, result);
        if(result.hasErrors()) {
            return REGISTRATION_PAGE;
        }

        RegistrationControllerLogger.logUserConstructedFromPostBody(user);
        User savedUser = userService.save(user);
        ApplicationDetails details = getApplicationDetails(request);
        publishUserRegistrationEvent(savedUser, details);

        return REGISTRATION_PAGE;
    }

    private ApplicationDetails getApplicationDetails(HttpServletRequest request) {

        String contextPath = request.getContextPath();
        Locale locale = request.getLocale();

        return new ApplicationDetails(contextPath, locale);
    }

    private void publishUserRegistrationEvent(User savedUser, ApplicationDetails details) {
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser, details));
    }

    public ModelAndView getLoginModelAndView() {
        return new ModelAndView(LOGIN_PAGE);
    }

    private static class RegistrationControllerLogger {
        private static final Logger log = LoggerFactory.getLogger(GalleryController.class);

        private static void logUserConstructedFromPostBody(User user) {
            log.info("User was retrieved from the post method body and stored in the database: {}",
                    user);
        }
    }
}