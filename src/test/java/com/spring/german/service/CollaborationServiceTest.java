package com.spring.german.service;

import com.spring.german.exceptions.RepositoryNotSpecifiedException;
import com.spring.german.util.GitHubRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CollaborationServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private CollaborationService collaborationService;
    private MockHttpSession session;
    private GitHubRepository validGitHubRepository;
    private List<String> validTechnologyNames;

    @Before
    public void setUp() throws Exception {
        collaborationService = new CollaborationService();
        session = new MockHttpSession();
        validGitHubRepository = new GitHubRepository("Collaboratory", "serhiizem");
        validTechnologyNames = Stream.of("Travis Build", "Code Coverage", "Spring Thymeleaf", "Spring MVC",
                "Spring validation", "Gradle", "Spring Security", "Bootstrap", "Checkstyle Plugin")
                .collect(Collectors.toList());
    }

    @Test
    public void shouldPopulateSessionWithTechnologiesFromRepo() {
        collaborationService.populateSessionWithTechnologiesFromRepo(session, validGitHubRepository);
        List<String> technologiesFromSession = (List<String>) session.getAttribute("technologies");

        assertThat(technologiesFromSession, is(validTechnologyNames));
    }

    @Test
    public void shouldThrowAnErrorOnMissingRepositoryName() {
        exception.expect(RepositoryNotSpecifiedException.class);
        exception.expectMessage("You have to specify repository name");
        collaborationService.populateSessionWithTechnologiesFromRepo(session, null);
    }
}