package com.spring.german.service;

import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

    private DefaultUserService userService;

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserRepository userRepository;
    @Mock private VerificationTokenRepository tokenRepository;

    @Before
    public void setUp() throws Exception {
        userService = new DefaultUserService(passwordEncoder, userRepository);
    }

    @Test
    public void registerUser() throws Exception {
        given(this.userRepository.findBySsoId(anyString())).willReturn(null);
    }
}