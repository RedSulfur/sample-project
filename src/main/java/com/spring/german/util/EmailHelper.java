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
     *
     * @param user
     * @param event
     * @param token
     * @return
     */
    public HtmlContent constructEmailMessage(User user, OnRegistrationCompleteEvent event, String token) {

        String recipientAddress = user.getEmail();
        log.info("Destination email: {}", user.getEmail());
        log.info("You will prepend the following url to your link: {}", event.getAppUrl());
        log.info("JavaMailSender performs an email dispatch");

        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("message.registration.success", null, event.getLocale());
        log.info("Based on user's locale, the following message, was fetched " +
                "from the properties file: {{}, locale: {}}", message, event.getLocale());

        final Context ctx = new Context(event.getLocale());
        ctx.setVariable("name", user.getFirstName());
        ctx.setVariable("body", message + " \n" + "http://localhost:8080" + confirmationUrl);
        ctx.setVariable("imageResourceName", LOGO_NAME); // so that we can reference it from HTML
        String body = this.templateEngine.process("email-inlineimage", ctx);

        HtmlContent htmlContent = new HtmlContent(body, recipientAddress, subject);

        return htmlContent;
    }

    public void sendEmail(HtmlContent htmlContent) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setText(htmlContent.getBody(), true); // true = isHtml
            mimeMessageHelper.setTo(htmlContent.getRecipientAddress());
            mimeMessageHelper.setSubject(htmlContent.getSubject());
            mimeMessageHelper.setFrom("noreply@gmail.com");
            mimeMessageHelper.addInline(LOGO_NAME, new ClassPathResource("static/img/logo.png"));

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
