package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.exceptions.ReadmeNotFoundException;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.UserService;
import com.spring.german.util.GitHubRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CollaborationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CollaborationServiceTest.class);

    @Mock private ProjectRepository projectRepository;
    @Mock private UserService userService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private CollaborationService collaborationService;
    private MockHttpSession session;
    private GitHubRepository validGitHubRepository;
    private List<String> validTechnologies;

    @Before
    public void setUp() throws Exception {

        collaborationService = new CollaborationService(projectRepository, userService);
        session = new MockHttpSession();
        validTechnologies = Stream.of("Travis Build, Code Coverage, Spring Thymeleaf, Spring MVC," +
                " Spring validation, Gradle, Spring Security, Bootstrap, Checkstyle Plugin")
                .collect(Collectors.toList());
        validGitHubRepository = new GitHubRepository("Collaboratory", "serhiizem");
    }

    @Test
    public void shouldPopulateSessionWithTechnologiesFromRepo() {

        collaborationService.populateSessionWithTechnologiesFromRepo(session, validGitHubRepository);
        List<String> technologiesFromSession = (List<String>) session.getAttribute("technologies");

        assertThat(technologiesFromSession.toString(), is(validTechnologies.toString()));
    }

    @Test
    public void shouldThrowAnErrorOnMissingRepositoryName() {
        exception.expect(NullPointerException.class);
        collaborationService.populateSessionWithTechnologiesFromRepo(session, null);
    }
}