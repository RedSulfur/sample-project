package com.spring.german.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean private RegistrationController controller;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
                .param("email", "john@doe.com"))

                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("user", hasProperty("ssoId", is("nickname"))))
                .andExpect(model().attribute("user", hasProperty("password", is("pass"))))
                .andExpect(model().attribute("user", hasProperty("email", is("john@doe.com"))))
                .andExpect(forwardedUrl(null));
    }

    @Test
    public void shouldRespondWithAnErrorOnMissingSsoId()
            throws Exception {
        mvc.perform(post("/registration").with(user("admin").password("pass")
                .roles("ADMIN", "USER")).contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "")
                .param("password", "password")
                .param("email", "john@doe.com"))

                .andExpect(view().name("registration"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "ssoId"))
                .andExpect(model().attributeHasFieldErrorCode("user", "ssoId", "NotEmpty"))
                .andExpect(model().errorCount(1));
    }

    @Test
    public void shouldRespondWithAnErrorOnEmptyPassword()
            throws Exception {
        mvc.perform(post("/registration").with(user("admin").password("pass")
                .roles("ADMIN", "USER")).contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "username")
                .param("password", "")
                .param("email", "user@gmail.com"))

                .andExpect(view().name("registration"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(model().attributeHasFieldErrorCode("user", "password",
                        anyOf(is("NotEmpty"), is("Size"))))
                .andExpect(model().errorCount(2));
    }

    @Test
    public void shouldRespondWithAnErrorIfPasswordIsTooShort()
            throws Exception {
        mvc.perform(post("/registration").with(user("admin").password("pass")
                .roles("ADMIN", "USER")).contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "username")
                .param("password", "123")
                .param("email", "user@gmail.com"))

                .andExpect(view().name("registration"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "password"))
                .andExpect(model().attributeHasFieldErrorCode("user", "password", is("Size")))
                .andExpect(model().errorCount(1));
    }

    @Test
    public void shouldRespondWithAnErrorOnMalformedEmail()
            throws Exception {
        mvc.perform(post("/registration").with(user("admin").password("pass")
                .roles("ADMIN", "USER")).contentType(MediaType.APPLICATION_JSON)
                .param("ssoId", "username")
                .param("password", "password")
                .param("email", "$malformed$###"))

                .andExpect(view().name("registration"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("user", "email"))
                .andExpect(model().attributeHasFieldErrorCode("user", "email", "Email"))
                .andExpect(model().errorCount(1));
    }
}