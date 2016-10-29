package com.spring.german.service;

import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

public class DefaultUserServiceTest {

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserRepository userRepository;
    @Mock private VerificationTokenRepository tokenRepository;

    @InjectMocks
    private DefaultUserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerUser() throws Exception {
        given(this.userRepository.findBySsoId(anyString())).willReturn(null);
        userService.getEntityByKey(anyString());
    }
}