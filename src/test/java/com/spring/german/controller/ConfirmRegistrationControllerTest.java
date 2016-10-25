package com.spring.german.controller;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.repository.UserRepository;
import com.spring.german.service.VerificationTokenService;
import com.spring.german.service.interfaces.Searching;
import com.spring.german.service.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Locale;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfirmRegistrationController.class)
public class ConfirmRegistrationControllerTest {

    private VerificationToken validVerificationToken;
    private User validUser;
    private static final String VALID_TOKEN = "non-expired";

    @Autowired private MockMvc mvc;
    @Autowired private MockHttpSession session;

    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private UserRepository userRepository;
    @MockBean private Searching<VerificationToken> tokenSearching;
    @MockBean private UserService userService;
    @MockBean private VerificationTokenService verificationTokenService;


    @Before
    public void setUp() {
        validUser = new User();
        validVerificationToken = new VerificationToken(VALID_TOKEN, validUser);
        validVerificationToken.setExpiryDate(new Date());
    }

    @Test
    public void confirmRegistration() throws Exception {

        given(tokenSearching.searchEntityByKey(anyString()))
                .willReturn(validVerificationToken);

        mvc.perform(get("/registrationConfirm")
                .requestAttr("token", VALID_TOKEN)
                .session(session).locale(Locale.ENGLISH))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("gallery"));

        verify(userService, times(1)).updateUser(anyObject());
    }
}