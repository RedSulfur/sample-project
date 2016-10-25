package com.spring.german.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);
    private static final String LOGO_NAME = "logo.png";

    private MessageSource messages;
    private JavaMailSender mailSender;

    @Autowired
    public EmailUtil(MessageSource messages,
                     JavaMailSender mailSender) {
        this.messages = messages;
        this.mailSender = mailSender;
    }
    /**
     * Sends a MIME style email message after filling it with all the required
     * for user information
     */
    //TODO: SEPARATE OBJECT?
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


