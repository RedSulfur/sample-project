package com.spring.german.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
public class ErrorMessages {

    @Value("${username.duplicate}")
    public static String DUPLICATE_USERNAME;
}
