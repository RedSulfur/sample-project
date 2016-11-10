package com.spring.german.util;

public class Email {

    private final String subject;
    private final String recipientAddress;
    private final String body;

    public Email(String subject, String recipientAddress, String body) {
        this.subject = subject;
        this.recipientAddress = recipientAddress;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public String getBody() {
        return body;
    }
}