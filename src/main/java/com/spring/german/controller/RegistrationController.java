package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.registration.OnRegistrationCompleteEvent;
import com.spring.german.service.UserService;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrationController {

    public static final String REGISTRATION_PAGE = "/registration";

    Logger log = LoggerFactory.getLogger(GalleryController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator validator;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    ApplicationEventPublisher eventPublisher;

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

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute(value = "user")
                                   @Validated @Valid User user,
                               BindingResult result,
                               Model model, HttpServletRequest request) {

        validator.validate(user, result);
        if(result.hasErrors()) {
            return REGISTRATION_PAGE;
        }

        User savedUser = userService.save(user);
        log.info("User was retrieved from the post method body and stored in the database: {}", savedUser);
        log.info("Locale: {}", request.getLocale());
        log.info("AppUrl: {}", request.getRequestURL());

        try {
            String appUrl = request.getRequestURL().toString();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent
                    (savedUser, request.getLocale(), appUrl));
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            List<String> list = new ArrayList<>();
            for(int i = 0; i < stackTrace.length; i ++) {
                list.add(stackTrace[i].toString() + "\n");
            }
            log.info("Error description: {}", list.toString());
            return "access-denied";
        }

        /**
         * TODO: Change this page to the display result page when it is ready
         */
        return REGISTRATION_PAGE;
    }
}
