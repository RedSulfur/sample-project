package com.spring.german.validation;

import com.spring.german.entity.User;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.service.interfaces.UserService;
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

    private static final Logger log = LoggerFactory.getLogger(UserValidator.class);

    private Searching<User> userSearching;
    private UserService userService;
    private MessageSource messages;

    @Autowired
    public UserValidator(Searching<User> userSearching,
                         UserService userService,
                         MessageSource messages) {
        this.userSearching = userSearching;
        this.userService = userService;
        this.messages = messages;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;
        log.info("UserValidator processes the following user: " + user.toString());

        Optional optionalBySsoId = Optional.ofNullable(userSearching.getEntityByKey(user.getSsoId()));
        Optional optionalByEmail = Optional.ofNullable(userService.findByEmail(user.getEmail()));

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