package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.registration.OnRegistrationCompleteEvent;
import com.spring.german.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegistrationController {

    Logger log = LoggerFactory.getLogger(GalleryController.class);

    @Autowired
    UserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

  /*  @ModelAttribute("user")
    public User getUser() {
        return new User();
    }*/

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
