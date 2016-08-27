package com.spring.german.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = { "com.spring.german.registration" })
@PropertySource("classpath:email.properties")
public class AppConfig {

    Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Autowired
    private Environment env;


     @Bean
     public MessageSource messageSource() {
         final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
         messageSource.setBasename("classpath:messages");
         messageSource.setUseCodeAsDefaultMessage(true);
         messageSource.setDefaultEncoding("UTF-8");
         messageSource.setCacheSeconds(0);
         return messageSource;
     }

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() {
        final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();

        try {
            mailSenderImpl.setHost(env.getRequiredProperty("smtp.host"));
            mailSenderImpl.setPort(env.getRequiredProperty("smtp.port", Integer.class));
            //mailSenderImpl.setProtocol(env.getRequiredProperty("smtp.protocol"));
            mailSenderImpl.setUsername(env.getRequiredProperty("smtp.username"));
            mailSenderImpl.setPassword(env.getRequiredProperty("smtp.password"));
        } catch (IllegalStateException e) {
            log.error("Could not resolve email.properties.  See email.properties.sample");
            throw e;
        }
        log.info("Mail sender properties were set to: {host:{}, port:{}, username:{}, " +
                "password:{}}", mailSenderImpl.getHost(), mailSenderImpl.getPort(),
                mailSenderImpl.getUsername(), mailSenderImpl.getPassword());
        final Properties javaMailProps = new Properties();
        javaMailProps.put("mail.smtp.auth", true);
        javaMailProps.put("mail.smtp.starttls.enable", true);
        log.info("Additional mail sender properties: {}", javaMailProps.toString());
        mailSenderImpl.setJavaMailProperties(javaMailProps);
        return mailSenderImpl;
    }
}