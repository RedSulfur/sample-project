package com.spring.german.service;

import com.spring.german.entity.User;
import com.spring.german.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.spring.german.entity.State.ACTIVE;
import static com.spring.german.service.ProjectTestUtil.EXISTING_ID;
import static com.spring.german.service.ProjectTestUtil.VALID_EMAIL;
import static com.spring.german.service.ProjectTestUtil.VALID_SSO_ID;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserServiceTest {

    private User validUser;
    private DefaultUserService userService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userService = new DefaultUserService(passwordEncoder, userRepository);
        validUser = ProjectTestUtil.getValidUser();
    }

    @Test
    public void shouldSearchForUser() {
        given(userRepository.findBySsoId(anyString())).willReturn(validUser);

        userService.getUserBySsoId(VALID_SSO_ID);

        verify(userRepository, times(1)).findBySsoId(VALID_SSO_ID);
        assertThat(userService.getUserBySsoId(VALID_SSO_ID), is(validUser));             //TODO: does order of verify and assert matters?
    }

    @Test
    public void shouldEncryptPasswordBeforeSavingUser() {
        String oldPassword = validUser.getPassword();
        when(userRepository.save((User) anyObject()))
                .then(returnsFirstArg());

        User savedUser = userService.save(validUser);
        String passwordAfterUserWasSaved = savedUser.getPassword();

        verify(userRepository, times(1)).save((User) anyObject());
        assertNotEquals("User password should be encrypted before storage",
                passwordAfterUserWasSaved, oldPassword);
    }

    @Test
    public void shouldSearchByEmail() {
        given(userRepository.findByEmail(anyString())).willReturn(validUser);

        userService.getByEmail(VALID_EMAIL);

        verify(userRepository, times(1)).findByEmail(VALID_EMAIL);
    }

    @Test
    public void shouldUpdateUserState() {
        when(userRepository.save((User) anyObject()))
                .then(returnsFirstArg());

        User updatedUser = userService.updateUserState(validUser);

        assertThat(updatedUser.getState(), is(ACTIVE.getState()));
    }

    @Test
    public void shouldFindUserOnExistingId() {
        given(userRepository.findOne(EXISTING_ID)).willReturn(validUser);

        assertNotNull("findOne method did not find a User when it actually existed",
                userService.getById(EXISTING_ID));

        verify(userRepository, times(1)).findOne(EXISTING_ID);
    }
}