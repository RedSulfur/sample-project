package com.spring.german.util;

import com.spring.german.entity.User;
import com.spring.german.registration.OnRegistrationCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Component
public class EventHandler {

    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);
    private static final String LOGO_NAME = "logo.png";

    private TemplateEngine templateEngine;
    private MessageSource messages;

    @Autowired
    public EventHandler(TemplateEngine templateEngine,
                        MessageSource messages) {
        this.templateEngine = templateEngine;
        this.messages = messages;
    }

    /**
     * @param event object that is used to determine user's current locale
     * @param token string that is used to construct a unique registration
     *              confirmation link
     */
    public String getEmailBody(OnRegistrationCompleteEvent event,
                                       String token) {

        Locale locale = event.getDetails().getLocale();

        String mainPart = messages.getMessage("message.registration.success", null, locale);
        String confirmationUrl
                = event.getDetails().getAppUrl() + "/registrationConfirm?token=" + token;

        return this.constructMessage(mainPart, confirmationUrl);
    }

    private String constructMessage(String message, String confirmationUrl) {

        StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append(message);
        emailBuilder.append(" \n");
        emailBuilder.append("http://localhost:8080");
        emailBuilder.append(confirmationUrl);

        return emailBuilder.toString();
    }

    /**
     * Evaluates provided parameters in order to produce
     * a ready to dispatch {@link Email} object
     *
     * @param user object that is used to extract user's email address
     */
    public Email constructEmailForUser(String emailBody, User user) {

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";

        final Context ctx = new Context();
        ctx.setVariable("name", user.getSsoId());
        ctx.setVariable("body", emailBody);
        ctx.setVariable("imageResourceName", LOGO_NAME);
        String processedContext = this.templateEngine.process("email-inlineimage", ctx);

        return new Email(recipientAddress, subject, processedContext);
    }
}
