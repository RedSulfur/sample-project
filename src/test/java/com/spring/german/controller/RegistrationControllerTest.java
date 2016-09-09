package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.entity.UserProfile;
import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.UserServiceImpl;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;

import static org.mockito.Matchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

//    @Autowired
//    private WebApplicationContext context;

    //@Autowired
    //private Filter springSecurityFilterChain;

    @MockBean private PasswordEncoder               passwordEncoder;
    @MockBean private UserRepository                userRepository;
    @MockBean private VerificationTokenRepository   tokenRepository;
    @MockBean private UserServiceImpl               userService;
    @MockBean private ApplicationEventPublisher     eventPublisher;
    @MockBean private RegistrationController        controller;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldPopulateModelOnGetRequestToRegistration()
            throws Exception {

        this.mvc.perform(get("/registration")
                .with(user("admin")
                .password("pass")
                .roles("ADMIN", "USER")))
                .andExpect(status().isOk());

//        this.mvc.perform(get("/registration")
//                .with(user("admin")
//                .password("pass")
//                .roles("USER","ADMIN")));
//                .andExpect(model().attribute("user", Matchers.instanceOf(User.class)));
    }
}