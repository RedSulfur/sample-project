package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.exceptions.TokenNotFoundException;
import com.spring.german.repository.VerificationTokenRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultVerificationTokenServiceTest {

    public static final String VALID_TOKEN = "valid-token";
    private DefaultVerificationTokenService tokenService;
    private User validUser;

    @Mock private VerificationTokenRepository tokenRepository;

    @Rule public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        tokenService = new DefaultVerificationTokenService(tokenRepository);
        validUser = new User();
    }
    @Test
    public void shouldThrowExceptionOnMissingKey() {
        exception.expect(TokenNotFoundException.class);
        exception.expectMessage("User not Found");
        tokenService.getEntityByKey(null);
    }

    @Test
    public void shouldSaveTokenOnce() {
        tokenService.createVerificationToken(validUser, VALID_TOKEN);

    }
}