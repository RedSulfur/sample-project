package com.spring.german.util;

public class HtmlContent {

    private final String body;
    private final String recipientAddress;
    private final String subject;

    public HtmlContent(String body, String recipientAddress, String subject) {
        this.body = body;
        this.recipientAddress = recipientAddress;
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public String getSubject() {
        return subject;
    }
}