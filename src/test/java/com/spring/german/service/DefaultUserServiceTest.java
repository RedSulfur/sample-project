package com.spring.german.service;

import com.spring.german.entity.State;
import com.spring.german.entity.User;
import com.spring.german.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.jws.soap.SOAPBinding;
import java.util.HashSet;

import static com.spring.german.entity.State.ACTIVE;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
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
//        when(userRepository.save(Matchers.any())).then(returnsFirstArg());
//        assertThat(userRepository.save(existingUser).getState(), is(ACTIVE.getState()));
    }
}