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
     * Processes an object that will contain a confirmation endpoint
     * which is appended by a unique UUID token.
     * Returned object encapsulates user's email which will
     * be used for the further email dispatch.
     *
     * @param user  object that is used to extract user's email address
     * @param event object that is used to determine user's current locale
     * @param token string that is used to construct a unique registration
     *              confirmation link
     */
    public HtmlContent processEvent(User user,
                                    OnRegistrationCompleteEvent event,
                                    String token) {

        String recipientAddress = user.getEmail();
        Locale locale = event.getLocale();
        log.info("Destination email: {}", recipientAddress);

        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.registration.success", null, locale);
        log.info("Based on user's locale, the following message, was fetched " +
                "from the properties file: {{}, locale: {}}", message, locale);

        final Context ctx = new Context(locale);
        ctx.setVariable("name", user.getSsoId());
        ctx.setVariable("body", message + " \n" + "http://localhost:8080" + confirmationUrl);
        ctx.setVariable("imageResourceName", LOGO_NAME);
        String body = this.templateEngine.process("email-inlineimage", ctx);

        //TODO: 1!
        HtmlContent htmlContent = new HtmlContent(body, recipientAddress, subject);

        return htmlContent;
    }
}
