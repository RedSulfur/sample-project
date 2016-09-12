package com.spring.german.validation;

import com.spring.german.controller.GalleryController;
import com.spring.german.entity.User;
import com.spring.german.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Locale;
import java.util.Optional;

@Component
@EnableConfigurationProperties
public class UserValidator implements Validator {

    Logger log = LoggerFactory.getLogger(GalleryController.class);

    @Autowired
    private UserService service;

    @Autowired
    private MessageSource messages;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;
        log.info("UserValidator processes the following user: " + user.toString());

        Optional optionalBySsoId = Optional.ofNullable(service.findBySso(user.getSsoId()));
        Optional optionalByEmail = Optional.ofNullable(service.findByEmail(user.getEmail()));

        if (optionalByEmail.isPresent()) {
            errors.rejectValue("email", "Registered Email",
                    messages.getMessage("email.taken", null, new Locale("en_US")));
        }
        if (optionalBySsoId.isPresent()) {
            errors.rejectValue("ssoId", "Duplicate Username",
                    messages.getMessage("username.duplicate", null, new Locale("en_US")));
        }
    }
}