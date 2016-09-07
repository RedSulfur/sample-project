package com.spring.german.controller;

import com.spring.german.repository.UserRepository;
import com.spring.german.repository.VerificationTokenRepository;
import com.spring.german.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository tokenRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerUser() throws Exception {
        given(this.userRepository.findBySsoId(anyString())).willReturn(null);
        userService.findBySso(anyString());
    }
}