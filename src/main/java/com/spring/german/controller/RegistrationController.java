package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.registration.OnRegistrationCompleteEvent;
import com.spring.german.repository.FilmRepository;
import com.spring.german.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrationController {

    Logger log = LoggerFactory.getLogger(HelloWorldController.class);

    @Qualifier("userService")
    @Autowired
    UserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute(value = "user") User user,
                               Model model, WebRequest request) {
        User savedUser = userService.save(user);
        log.info("User was retrieved from the post method body and stored in the database: {}", savedUser);

        try {
            String appUrl = request.getContextPath();
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

        return "registration";
    }
}
