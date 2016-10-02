package com.spring.german.util;

import com.spring.german.entity.User;
import com.spring.german.registration.OnRegistrationCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Component
public class EmailHelper {

    private static final Logger log = LoggerFactory.getLogger(EmailHelper.class);

    private static final String LOGO_NAME = "logo.png";

    private TemplateEngine templateEngine;
    private MessageSource messages;
    private JavaMailSender mailSender;

    /**
     * The instance of the TemplateEngine class is provided by Spring Boot
     * Thymeleaf auto configuration. All is needed is to call the process()
     * method which expects the name of the template that we want to use and
     * the context object that acts as a container for our model.
     */
    @Autowired
    public EmailHelper(TemplateEngine templateEngine,
                       MessageSource messages,
                       JavaMailSender mailSender) {
        this.messages = messages;
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    /**
     * Constructs an {@link HtmlContent} object that has a string
     * in its field containing the result of evaluating the specified
     * template with the provided context. This object contains also
     * a confirmation endpoint which is appended by a unique UUID token.
     * The returning object will encapsulate a user's email which will
     * be used for the further email dispatch.
     *
     * @param user  object that is used to extract user's email address
     * @param event object is used to determine user's current locale
     * @param token string that is used to construct a unique registration
     *              confirmation link
     *
     * @return      fully constructed instance of an {@link HtmlContent} class
     */
    public HtmlContent constructEmailMessage(User user, OnRegistrationCompleteEvent event, String token) {

        String recipientAddress = user.getEmail();
        Locale locale = event.getLocale();
        log.info("Destination email: {}", user.getEmail());
        log.info("You will prepend the following url to your link: {}", event.getAppUrl());
        log.info("JavaMailSender performs an email dispatch");

        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.registration.success", null, locale);
        log.info("Based on user's locale, the following message, was fetched " +
                "from the properties file: {{}, locale: {}}", message, locale);

        final Context ctx = new Context(locale);
        ctx.setVariable("name", user.getFirstName());
        ctx.setVariable("body", message + " \n" + "http://localhost:8080" + confirmationUrl);
        ctx.setVariable("imageResourceName", LOGO_NAME); // so that we can reference it from HTML
        String body = this.templateEngine.process("email-inlineimage", ctx);

        HtmlContent htmlContent = new HtmlContent(body, recipientAddress, subject);

        return htmlContent;
    }

    /**
     * Method creates a MIME style email message. Provides a recipient address
     * for the created message, specifies an inline image and a message body,
     * determines a subject of the message and an information about sender.
     * Performs an email dispatch.
     *
     * @param htmlContent an object that encapsulates all the required for an email dispatch
     *                    information
     */
    public void sendEmail(HtmlContent htmlContent) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setText(htmlContent.getBody(), true); // true = isHtml
            mimeMessageHelper.setTo(htmlContent.getRecipientAddress());
            mimeMessageHelper.setSubject(htmlContent.getSubject());
            mimeMessageHelper.setFrom("noreply@gmail.com");
            mimeMessageHelper.addInline(LOGO_NAME, new ClassPathResource("static/img/logo.png"));

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}