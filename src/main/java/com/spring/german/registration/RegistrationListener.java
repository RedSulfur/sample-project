package com.spring.german.registration;

import com.spring.german.entity.User;
import com.spring.german.service.interfaces.Creating;
import com.spring.german.util.EmailHelper;
import com.spring.german.util.HtmlContent;
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
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    Logger log = LoggerFactory.getLogger(RegistrationListener.class);

    private Creating creator;
    private EmailHelper emailHelper;

    @Autowired
    public RegistrationListener(Creating creator,
                                EmailHelper emailHelper) {
        this.creator = creator;
        this.emailHelper = emailHelper;
    }

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
     * Unique verification token is generated by static method UUID.randomUUID()
     * - Static factory to retrieve a type 4 (pseudo randomly generated) UUID.
     * The UUID is generated using a cryptographically strong pseudo random number
     * generator.
     *
     * @param event
     */
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        log.info("RegistrationListener accepts the following user: {}", event.getUser());

        String token = UUID.randomUUID().toString();
        creator.createVerificationToken(user, token);

        HtmlContent htmlContent = emailHelper.constructEmailMessage(user, event, token);

        emailHelper.sendEmail(htmlContent);
    }
}
