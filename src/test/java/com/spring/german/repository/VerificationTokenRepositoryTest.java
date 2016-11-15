package com.spring.german.repository;

import com.spring.german.entity.User;
import com.spring.german.entity.VerificationToken;
import com.spring.german.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static com.spring.german.util.TestUtil.VALID_TOKEN;
import static java.time.LocalDate.now;
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

    @Before
    public void setUp() throws Exception {
        validUser = TestUtil.getValidUser();
        userRepository.save(validUser);
    }

    @Test
    public void shouldCalculateExpiryDate() {
        VerificationToken verificationTokenToSave = new VerificationToken(VALID_TOKEN, validUser);

        VerificationToken savedToken = verificationTokenRepository
                .save(verificationTokenToSave);

        assertThat(savedToken.getExpiryDate(), is(now().plusDays(1)));
        assertThat(savedToken.getToken(), is(VALID_TOKEN));
    }

    @Test
    public void shouldFindSavedTokenByToken() {
        verificationTokenRepository.save(new VerificationToken(VALID_TOKEN, validUser));

        VerificationToken extractedToken = verificationTokenRepository.findByToken(VALID_TOKEN);
        assertThat(extractedToken.getToken(), is(VALID_TOKEN));
    }
}