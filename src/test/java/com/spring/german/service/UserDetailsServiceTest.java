package com.spring.german.service;

import com.spring.german.service.interfaces.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.spring.german.util.TestUtil.NON_EXISTENT_SSO_ID;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceTest {

    private UserDetailsService userDetailsService;

    @Mock private UserService userService;
    @Rule public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        userDetailsService = new UserDetailsService(userService);
    }

    @Test
    public void shouldTrowExceptionOnNonExistentUser() {
        when(userService.getUserBySsoId(anyString())).thenReturn(null);

        exception.expect(UsernameNotFoundException.class);
        exception.expectMessage("Username not found");

        userDetailsService.getEntityByKey(NON_EXISTENT_SSO_ID);
    }
}