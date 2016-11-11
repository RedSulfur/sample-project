package com.spring.german.service;

import com.spring.german.entity.State;
import com.spring.german.entity.User;
import com.spring.german.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static com.spring.german.entity.State.ACTIVE;
import static com.spring.german.entity.State.INACTIVE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

    private static final long EXISTING_ID = 1L;
    private User existingUser;

    private DefaultUserService userService;

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        existingUser =
                new User("default", "pass", "default@gmail.com", State.INACTIVE.getState(), new HashSet<>());
        userService = new DefaultUserService(passwordEncoder, userRepository);
    }

    @Test
    public void registerUser() throws Exception {
        given(userRepository.findBySsoId(anyString())).willReturn(null);
    }

    @Test
    public void shouldFindUserOnExistingId() {
        given(userRepository.findOne(EXISTING_ID)).willReturn(existingUser);

        assertNotNull("findOne method did not find a User when it actually existed",
                userService.getById(EXISTING_ID));
    }

    @Test
    public void shouldUpdateUserState() {
        when(userRepository.save((User) anyObject()))
                .then(AdditionalAnswers.returnsFirstArg());

        User updatedUser = userService.updateUserState(existingUser);

        assertThat(updatedUser.getState(), is(ACTIVE.getState()));
    }
}