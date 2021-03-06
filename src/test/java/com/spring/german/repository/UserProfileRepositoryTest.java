package com.spring.german.repository;

import com.spring.german.entity.UserProfile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    public void shouldFindAllUserProfilesWithSpecificAuthority() {

        UserProfile userProfile = userProfileRepository.findByType("USER");

        assertThat(userProfile.getType(), is("USER"));
    }
}
