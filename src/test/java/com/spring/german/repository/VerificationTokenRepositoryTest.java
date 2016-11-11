package com.spring.german.repository;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashSet;

import static com.spring.german.entity.State.ACTIVE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VerificationTokenRepositoryTest {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User validUser;
    private String validToken;

    @Before
    public void setUp() throws Exception {
        validUser = new User("default", "pass", "default@gmail.com", ACTIVE.getState(), new HashSet<>());
        validToken = "valid-token";
        userRepository.save(validUser);
    }

    @Test
    public void shouldCalculateExpiryDate() {
        VerificationToken savedToken = verificationTokenRepository
                .save(new VerificationToken(validToken, validUser));

        assertThat(savedToken.getExpiryDate(), is(LocalDate.now().plusDays(1)));
        assertThat(savedToken.getToken(), is(validToken));
    }

    @Test
    public void shouldFindSavedTokenByToken() {
        verificationTokenRepository.save(new VerificationToken(validToken, validUser));

        VerificationToken extractedToken = verificationTokenRepository.findByToken(validToken);
        assertThat(extractedToken.getToken(), is(validToken));
    }
}