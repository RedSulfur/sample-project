package com.spring.german.registration;

import com.spring.german.entity.User;
import com.spring.german.service.interfaces.Creating;
import com.spring.german.util.EmailUtil;
import com.spring.german.util.EventHandler;
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
public class RegistrationListener
        implements ApplicationListener<OnRegistrationCompleteEvent> {

    Logger log = LoggerFactory.getLogger(RegistrationListener.class);

    private Creating creator;
    private EmailUtil emailUtil;
    private EventHandler eventHandler;

    @Autowired
    public RegistrationListener(Creating creator,
                                EmailUtil emailUtil,
                                EventHandler eventHandler) {
        this.creator = creator;
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
        creator.createVerificationToken(user, token);

        HtmlContent htmlContent = eventHandler.processEvent(user, event, token);

        emailUtil.sendEmail(htmlContent);
    }
}
