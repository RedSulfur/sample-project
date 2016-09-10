package com.spring.german.util;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Class is used in {@link com.spring.german.config.SecurityConfiguration}
 * configuration class as a parameter for
 * {@code CsrfConfigurer#requireCsrfProtectionMatcher(RequestMatcher)}
 * method to enable csrf protection for all the endpoints except the given
 * one.
 */
public class UrlFilter implements RequestMatcher {

    private static final String[] ALLOWED_METHODS =
            new String[] {"GET"};

    private final String[] allowedUrls;

    public UrlFilter(String... allowedUrls) {
        this.allowedUrls = allowedUrls;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String method = request.getMethod();
        for(String allowedMethod : ALLOWED_METHODS) {
            if (allowedMethod.equals(method)) {
                return false;
            }
        }

        String uri = request.getRequestURI();
        for (String allowedUrl : allowedUrls) {
            if (uri.startsWith(allowedUrl)) {
                return false;
            }
        }
        return true;
    }
}
