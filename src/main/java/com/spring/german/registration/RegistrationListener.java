package com.spring.german.registration;

import com.spring.german.entity.User;
import com.spring.german.service.interfaces.VerificationTokenService;
import com.spring.german.util.EmailUtil;
import com.spring.german.util.EventHandler;
import com.spring.german.util.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Handles the OnRegistrationCompleteEvent
 */
@Component
public class RegistrationListener
        implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final Logger log = LoggerFactory.getLogger(RegistrationListener.class);

    private VerificationTokenService verificationTokenService;
    private EmailUtil emailUtil;
    private EventHandler eventHandler;

    @Autowired
    public RegistrationListener(VerificationTokenService tokenService,
                                EmailUtil emailUtil,
                                EventHandler eventHandler) {
        this.verificationTokenService = tokenService;
        this.emailUtil = emailUtil;
        this.eventHandler = eventHandler;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
            this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        log.info("RegistrationListener accepts the following user: {}", event.getUser());

        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(user, token);

        String emailBody = eventHandler.getEmailBody(event, token);
        Email email = eventHandler.constructEmailForUser(emailBody, user);

        emailUtil.sendEmail(email);
    }
}
