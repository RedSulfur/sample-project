package com.spring.german.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * Tells Spring Security's authentication manager to perform
         * authentications within MemoryAuthentication
         */
        auth.inMemoryAuthentication().withUser("mkyong").password("123456").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers("/", "/home").permitAll()
                /**
                 * Any URL that starts with "/gallery" will be restricted to users who have the role
                 * "ROLE_ADMIN". You will notice that since we are invoking the hasRole method we
                 * do not need to specify the "ROLE_" prefix.
                 */
                .antMatchers("/gallery").access("hasRole('ROLE_ADMIN')")
                /**
                 * This code creates a custom login page with ‘/login’ url, which will
                 * accept ssoId as username and password Http request parameters.
                 */
                .and().formLogin().loginPage("/login")
                .usernameParameter("ssoId").passwordParameter("password")
                .and().csrf()
                /**
                 * will catch all 403 [http access denied] exceptions and display our user
                 * defined page instead of showing default HTTP 403 page [ which is not so helpful anyway].
                 */
                .and().exceptionHandling().accessDeniedPage("/Access_Denied");

    }

}
