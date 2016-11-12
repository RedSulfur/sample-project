package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.State;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.UserService;
import com.spring.german.util.GitHubRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CollaborationServiceTest {

    public static final String VALID_USERNAME = "valid username";

    @Autowired
    private ProjectRepository projectRepository;

    @Mock private UserService userService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private CollaborationService collaborationService;
    private MockHttpSession session;
    private GitHubRepository validGitHubRepository;
    private List<String> validTechnologyNames;
    private User validUser;

    @Before
    public void setUp() throws Exception {
        collaborationService = new CollaborationService(projectRepository, userService);
        session = new MockHttpSession();
        validTechnologyNames = Stream.of("Travis Build", "Code Coverage", "Spring Thymeleaf", "Spring MVC",
                "Spring validation", "Gradle", "Spring Security", "Bootstrap", "Checkstyle Plugin")
                .collect(Collectors.toList());
        validGitHubRepository = new GitHubRepository("Collaboratory", "serhiizem");
        validUser = new User("default", "pass", "default@gmail.com", State.ACTIVE.getState(), new HashSet<>());
    }

    @Test
    public void shouldPopulateSessionWithTechnologiesFromRepo() {

        collaborationService.populateSessionWithTechnologiesFromRepo(session, validGitHubRepository);
        List<String> technologiesFromSession = (List<String>) session.getAttribute("technologies");

        assertThat(technologiesFromSession.toString(), is(validTechnologyNames.toString()));
    }

    @Test
    public void shouldThrowAnErrorOnMissingRepositoryName() {
        exception.expect(NullPointerException.class); //TODO: This exception should not be this kind of type
        collaborationService.populateSessionWithTechnologiesFromRepo(session, null);
    }

    @Test
    public void shouldSaveProjectWithTechnologies() {
        given(userService.getUserBySsoId(anyString())).willReturn(validUser);

        collaborationService.saveProjectWithTechnologies(VALID_USERNAME, validTechnologyNames);

        Project lastSavedProject = projectRepository.findOne(10L);

        lastSavedProject.getTechnologies().stream()
                .map(Technology::getName).collect(Collectors.toList())
                .containsAll(validTechnologyNames);
    }
}