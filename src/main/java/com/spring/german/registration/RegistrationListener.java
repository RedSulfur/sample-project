package com.spring.german.registration;

import com.spring.german.entity.User;
import com.spring.german.service.UserService;
import com.spring.german.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import java.util.UUID;

/**
 * Handles the OnRegistrationCompleteEvent
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    Logger log = LoggerFactory.getLogger(RegistrationListener.class);

    @Autowired
    private UserService service;
    @Autowired
    private MessageSource messages;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    /**
     * TODO: c-p#1
     * confirmRegistration method will receive the OnRegistrationCompleteEvent,
     * extract all the necessary User information from it, create the
     * verification token, persist it, and then send it as a parameter in the
     * “Confirm Registration” link.
     *
     * As was mentioned above, any javax.mail.AuthenticationFailedException
     * thrown by JavaMailSender will be handled by the controller.
     * @param event
     */
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        log.info("You enter confirmRegistration with the user: {}", event.getUser());
        /**
         * UUID - Universally Unique Identifier.
         * UUID.randomUUID() - Static factory to retrieve a type 4 (pseudo
         * randomly generated) UUID. The UUID is generated using a
         * cryptographically strong pseudo random number generator.
         */
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        log.info("Destination email: {}", user.getEmail());
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/regitrationConfirm.html?token=" + token;
        log.info("getMessage method from MessageSource object");
        //String message = messages.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        //email.setText(message + " rn" + "http://localhost:8080" + confirmationUrl);
        email.setText("http://localhost:8080" + confirmationUrl);
        log.info("JavaMailSender performs an email dispatch: {}");
        mailSender.send(email);
    }
}
