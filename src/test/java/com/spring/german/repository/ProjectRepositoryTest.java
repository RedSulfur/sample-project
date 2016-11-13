package com.spring.german.repository;

import com.spring.german.entity.Project;
import com.spring.german.entity.State;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {

    public static final String VALID_USERNAME = "valid username";
    public static final List<String> TECHNOLOGIES_TO_SEARCH_BY = Arrays.asList("Git", "Gradle");

    private List<String> validTechnologyNames;
    private User validUser;

    @Autowired
    private ProjectRepository projectRepository;

    @Before
    public void setUp() throws Exception {
        validTechnologyNames = Stream.of("Travis Build", "Code Coverage", "Spring Thymeleaf", "Spring MVC",
                "Spring validation", "Gradle", "Spring Security", "Bootstrap", "Checkstyle Plugin")
                .collect(Collectors.toList());
        validUser = new User("default", "pass", "default@gmail.com", State.ACTIVE.getState(), new HashSet<>());
    }

    @Test
    public void shouldFindAllTheProjectsThatCorrespondToTheInput() {

        List<Project> projectsByTechnologyNames = projectRepository
                .findDistinctByTechnologiesNameIn(TECHNOLOGIES_TO_SEARCH_BY);

        List<String> technologies = projectsByTechnologyNames.stream()
                .map(Project::getTechnologies)
                .flatMap(List::stream)
                .map(Technology::getName)
                .collect(Collectors.toList());

        assertThat(projectsByTechnologyNames.size(), Matchers.is(4));
        assertThat(technologies, Matchers.hasItems("Git", "Gradle"));
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