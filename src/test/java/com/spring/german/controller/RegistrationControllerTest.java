package com.spring.german.controller;

import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private VerificationTokenRepository tokenRepository;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private ApplicationEventPublisher eventPublisher;
    @MockBean
    private RegistrationController controller;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldNotRedirectOnGetRequestToRegistrationPage()
            throws Exception {

        this.mvc.perform(get("/registration")
                .with(user("admin")
                        .password("pass")
                        .roles("ADMIN", "USER")))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl(null));
    }

    @Test
    public void shouldPopulateModelOnPostMethodToRegistrationPage()
            throws Exception {

        this.mvc.perform(post("/registration")
                .with(user("admin")
                .password("pass")
                .roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "nickname")
                .param("password", "pass")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "john@doe.com"))

                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("user", hasProperty("ssoId", is("nickname"))))
                .andExpect(model().attribute("user", hasProperty("password", is("pass"))))
                .andExpect(model().attribute("user", hasProperty("firstName", is("John"))))
                .andExpect(model().attribute("user", hasProperty("lastName", is("Doe"))))
                .andExpect(model().attribute("user", hasProperty("email", is("john@doe.com"))))
                .andExpect(forwardedUrl(null));
    }

    @Test
    public void sf()
            throws Exception {
        //mvc.perform();

    }
}