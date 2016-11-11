package com.spring.german.util;

public class Email {

    private final String recipientAddress;
    private final String subject;
    private final String body;

    public Email(String recipientAddress, String subject, String body) {
        this.recipientAddress = recipientAddress;
        this.subject = subject;
        this.body = body;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}