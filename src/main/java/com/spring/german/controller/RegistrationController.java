package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.registration.OnRegistrationCompleteEvent;
import com.spring.german.service.interfaces.UserService;
import com.spring.german.validation.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrationController {

    public static final String REGISTRATION_PAGE = "/registration";

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
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView model) {
        model.setViewName("/registration");
        model.addObject(new User());
        return model;
    }

    //TODO: Change return page to the display result page when result page is ready

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute(value = "user")
                                   @Validated @Valid User user,
                               BindingResult result,
                               Model model, HttpServletRequest request) {

        validator.validate(user, result);
        if(result.hasErrors()) {
            return REGISTRATION_PAGE;
        }

        RegistrationControllerLogger.logUserConstructedFromPostBody(user);

        User savedUser = userService.save(user);

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent
                    (savedUser, request.getLocale(), appUrl));
        } catch (Exception e) {
            String errorDescription = constructErrorDescription(e);
            RegistrationControllerLogger.logErrorDuringPublishingEvent(errorDescription);
            return "access-denied";
        }

        return REGISTRATION_PAGE;
    }

    private String constructErrorDescription(Exception e) {

        StackTraceElement[] stackTrace = e.getStackTrace();
        List<String> list = new ArrayList<>();
        for (StackTraceElement aStackTrace : stackTrace) {
            list.add(aStackTrace.toString() + "\n");
        }
        String errorDescription = list.toString();

        return errorDescription;
    }

    /**
     * Provides helper methods for its outer class {@see RegistrationController}
     */
    private static class RegistrationControllerLogger {

        private static final Logger log = LoggerFactory.getLogger(GalleryController.class);

        private static void logUserConstructedFromPostBody(User user) {
            log.info("User was retrieved from the post method body and stored in the database: {}",
                    user);
        }

        private static void logErrorDuringPublishingEvent(String errorDescription) {
            log.info("Error description: {}", errorDescription);
        }
    }
}
