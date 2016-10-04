package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.validation.UserValidator;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private UserRepository userRepository;
    @MockBean private VerificationTokenRepository tokenRepository;
    @MockBean private Searching<User> userSearching;
    @MockBean private ApplicationEventPublisher eventPublisher;
    @MockBean private RegistrationController controller;
    @MockBean private UserValidator validator;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

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
    public void shouldRespondWithMultipleErrorsOnMissingFirstAndLastName()
            throws Exception {
        mvc.perform(post("/registration").with(user("admin").password("pass")
                .roles("ADMIN", "USER")).contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "username")
                .param("password", "password")
                .param("firstName", "")
                .param("lastName", "")
                .param("email", "user@gmail.com"))

                .andExpect(view().name("registration"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "firstName"))
                .andExpect(model().attributeHasFieldErrorCode("user", "firstName", anyOf(is("NotEmpty"), is("Size"))))
                .andExpect(model().attributeHasFieldErrors("user", "lastName"))
                .andExpect(model().attributeHasFieldErrorCode("user", "lastName", anyOf(is("NotEmpty"), is("Size"))))
                .andExpect(model().errorCount(4));
    }

    @Test
    public void shouldRespondWithAnErrorOnMalformedEmail()
            throws Exception {
        mvc.perform(post("/registration").with(user("admin").password("pass")
                .roles("ADMIN", "USER")).contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "username")
                .param("password", "password")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "$malformed$###"))

                .andExpect(view().name("registration"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "Email"))
                .andExpect(model().errorCount(1));
    }

    @Test
    public void shouldRespondWithAnErrorOnMissingSsoId()
            throws Exception {
        mvc.perform(post("/registration").with(user("admin").password("pass")
                .roles("ADMIN", "USER")).contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "")
                .param("password", "password")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "john@doe.com"))

                .andExpect(view().name("registration"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "ssoId"))
                .andExpect(model().attributeHasFieldErrorCode("user", "ssoId", "NotEmpty"))
                .andExpect(model().errorCount(1));
    }

    @Test
    public void shouldRespondWithAnErrorOnMissingPassword()
            throws Exception {
        mvc.perform(post("/registration").with(user("admin").password("pass")
                .roles("ADMIN", "USER")).contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "nickname")
                .param("password", "")
                .param("firstName", "John")
                .param("lastName", "Doe")
                .param("email", "john@doe.com"))

                .andExpect(view().name("registration"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(model().errorCount(1));
    }
}