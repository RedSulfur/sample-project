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

import static com.spring.german.util.ErrorMessages.DUPLICATE_USERNAME;

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

        log.info("Error message fetched: {}", DUPLICATE_USERNAME);
        User user = (User) target;
        User currentUser = service.findBySso(user.getSsoId());
        if (currentUser.getSsoId().equals(user.getSsoId())) {
            errors.rejectValue("ssoId", "Duplicate Username",
                    messages.getMessage("username.duplicate", null, new Locale("en_US")));
        }
    }
}