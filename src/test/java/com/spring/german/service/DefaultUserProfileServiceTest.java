package com.spring.german.service;

import com.spring.german.entity.UserProfile;
import com.spring.german.exceptions.TokenNotFoundException;
import com.spring.german.repository.UserProfileRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserProfileServiceTest {

    public static final String VALID_TOKEN = "valid-token";
    private DefaultUserProfileService userProfileService;
    private UserProfile validUserProfile;

    @Rule public ExpectedException exception = ExpectedException.none();

    @Mock private UserProfileRepository userProfileRepository;

    @Before
    public void setUp() throws Exception {
        userProfileService = new DefaultUserProfileService(userProfileRepository);
        validUserProfile = new UserProfile();
    }

    @Test
    public void shouldThrowExceptionOnNonExistentToken() {
        exception.expect(TokenNotFoundException.class);
        exception.expectMessage("Requested token is not present in database");

        userProfileService.findByType(null);
    }
    
    @Test
    public void shouldReturnValidUserProfileOnExistingToken() {
        given(userProfileRepository.findByType(VALID_TOKEN))
                .willReturn(validUserProfile);

        UserProfile extractedUserProfile = userProfileService.findByType(VALID_TOKEN);

        assertThat(extractedUserProfile, is(validUserProfile));
    }

    @Test
    public void shouldReturnValidUserProfileOnExistingId() {
        given(userProfileRepository.findOne(anyLong()))
                .willReturn(validUserProfile);

        UserProfile extractedUserProfile = userProfileService.getById(1L);

        assertThat(extractedUserProfile, is(validUserProfile));
    }


}
