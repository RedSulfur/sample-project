package com.spring.german.config;

import com.spring.german.util.UrlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    @Qualifier("customUserDetailsServiceImplementation")
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Method ensures that any URL that starts with "/gallery" or "/collaborate" will
     * be restricted to users who have the role "ROLE_ADMIN".
     * Creates a custom login page with ‘/login’ url, which will
     * accept ssoId as username and password as http request parameters.
     * It will also ensure that instead of showing the default HTTP 403 page
     * when any 403 exception [http access denied] occurs a specific informative
     * error page will be shown.
     *
     * @param  http {@link HttpSecurity} object that is used to specify
     *              access restrictions to every endpoint in the project
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/", "/home", "/h2/**").permitAll()
                .antMatchers("/gallery", "/collaborate").access("hasRole('ROLE_ADMIN')")
                .and().formLogin().loginPage("/login")
                .usernameParameter("ssoId").passwordParameter("password")
                .and().csrf().requireCsrfProtectionMatcher(new UrlFilter("/h2"));
        http.headers().frameOptions().disable();
    }
}