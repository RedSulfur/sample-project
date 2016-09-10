package com.spring.german.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration for database access.
 */
@Configuration
@ComponentScan("com.spring.german")
@EnableTransactionManagement
@EnableJpaRepositories("com.spring.german.repository")
public class DataConfig {
}
