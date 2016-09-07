package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.entity.UserProfile;
import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.mockito.Matchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ApplicationContext applicationContext;

    //@Autowired
    //private Filter springSecurityFilterChain;

    @MockBean private PasswordEncoder               passwordEncoder;
    @MockBean private UserRepository                userRepository;
    @MockBean private VerificationTokenRepository   tokenRepository;
    @MockBean private UserServiceImpl               userService;
    @MockBean private ApplicationEventPublisher     eventPublisher;
    @MockBean private RegistrationController        controller;

    @Test
    public void registerUser() throws Exception {

        BDDMockito.given(this.userService.save(anyObject()))
                .willReturn(new User("valid_user",
                        "valid_password",
                        "valid_name",
                        "valid_last_name",
                        "valid_email",
                        "inactive",
                        new HashSet<UserProfile>()));

        this.mvc.perform(post("/registration").with(csrf()))
                .andExpect(redirectedUrl("/registration"));
    }

}