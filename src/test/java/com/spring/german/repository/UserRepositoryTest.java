package com.spring.german.repository;

import com.spring.german.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {

        Arrays.asList("Sam", "Cary", "Tom", "Jane", "Patrick")
                .forEach(
                        u -> userRepository
                                .save(new User("User" + u, "Pass" + u,
                                        u + "@gmail.com", "ACTIVE",
                                        new HashSet<>()))
                );

        Arrays.asList("Mark", "Jim", "Sally")
                .forEach(
                        u -> userRepository
                                .save(new User("User" + u, "Pass" + u,
                                        u + "@gmail.com", "INACTIVE",
                                        new HashSet<>()))
                );
    }

    @Test
    public void findBySsoShouldReturnUser() throws Exception {

        User obtainedUser = this.userRepository.findBySsoId("UserSam");
        assertThat(obtainedUser.getSsoId(), is("FNameSam"));
        assertThat(obtainedUser.getUserProfiles().isEmpty(), is(true));
    }

    @Test
    public void shouldFindAllTheActiveUsers() {

        List<User> activeUsers = userRepository.findByState("ACTIVE");
        assertEquals(5, activeUsers.size());
    }

    @Test
    public void deleteByStateShouldRemoveUsers() {
        List<User> inactive = userRepository.removeByState("INACTIVE");
        assertThat(inactive.size(), is(3));
    }
}

