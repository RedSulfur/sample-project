package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.exceptions.TokenNotFoundException;
import com.spring.german.repository.VerificationTokenRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.spring.german.util.TestUtil.VALID_TOKEN;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultVerificationTokenServiceTest {

    private DefaultVerificationTokenService tokenService;
    private User validUser;
    private VerificationToken existingVerificationToken;

    @Mock private VerificationTokenRepository tokenRepository;

    @Rule public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        tokenService = new DefaultVerificationTokenService(tokenRepository);
        validUser = new User();
        existingVerificationToken = new VerificationToken(VALID_TOKEN, validUser);
    }

    @Test
    public void shouldCreateAndSaveVerificationToken() {
        tokenService.createVerificationToken(validUser, VALID_TOKEN);

        verify(tokenRepository, times(1)).save((VerificationToken) anyObject());
    }

    @Test
    public void shouldThrowExceptionOnMissingKey() {
        exception.expect(TokenNotFoundException.class);
        exception.expectMessage("User not Found");
        tokenService.getEntityByKey(null);
    }

    @Test
    public void shouldSearchForTokenIfTokenNameWasNotNull() {
        given(tokenRepository.findByToken(anyString())).willReturn(existingVerificationToken);

        tokenService.getEntityByKey(VALID_TOKEN);

        verify(tokenRepository, times(1)).findByToken(anyString());
    }
}