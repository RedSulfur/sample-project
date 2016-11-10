package com.spring.german.registration;

import java.util.Locale;

public class ApplicationDetails {

    private final String appUrl;
    private final Locale locale;

    public ApplicationDetails(String appUrl, Locale locale) {
        this.appUrl = appUrl;
        this.locale = locale;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }
}
