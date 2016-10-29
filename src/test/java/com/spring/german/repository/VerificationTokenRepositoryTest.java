package com.spring.german.repository;

import com.spring.german.entity.VerificationToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VerificationTokenRepositoryTest {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    /**
     * In order to start test, referential and data integrity should somehow
     * be set to false as soon as {@link VerificationToken} object relies on
     * many other objects whose schemas define their fields to be not null.
     *
     * @throws Exception
     */
    @Test
    public void findByToken() throws Exception {

        //VerificationTokenService tokenToSave = verificationTokenRepository.save(new VerificationTokenService("placeholder"));

        //VerificationTokenService tokenFromDb = verificationTokenRepository.findOne(1L);
        //assertThat(tokenFromDb.getToken(), is("placeholder"));
    }
}