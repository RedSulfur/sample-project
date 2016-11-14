package com.spring.german.repository;

import com.spring.german.entity.Project;
import com.spring.german.entity.State;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.service.ProjectTestUtil;
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

import static com.spring.german.service.DefaultProjectServiceTest.VALID_USERNAME;
import static com.spring.german.service.ProjectTestUtil.getValidUser;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {

    public static final List<String> TECHNOLOGIES_TO_SEARCH_BY = Arrays.asList("Git", "Gradle");

    private List<String> validTechnologyNames;

    @Autowired
    private ProjectRepository projectRepository;

    @Before
    public void setUp() throws Exception {
        validTechnologyNames = Stream.of("Travis Build", "Code Coverage", "Spring Thymeleaf", "Spring MVC",
                "Spring validation", "Gradle", "Spring Security", "Bootstrap", "Checkstyle Plugin")
                .collect(toList());
    }

    @Test
    public void shouldFindAllTheProjectsThatCorrespondToTheInput() {

        List<Project> projectsByTechnologyNames = projectRepository
                .findDistinctByTechnologiesNameIn(TECHNOLOGIES_TO_SEARCH_BY);

        List<String> technologies = projectsByTechnologyNames.stream()
                .map(Project::getTechnologies)
                .flatMap(List::stream)
                .map(Technology::getName)
                .collect(toList());

        assertThat(projectsByTechnologyNames.size(), Matchers.is(4));
        assertThat(technologies, Matchers.hasItems("Git", "Gradle"));
    }

    @Test
    public void shouldSaveProjectWithTechnologies() {
        Project project = new Project("default", getValidUser());                                                     //TODO: Project name should be defined by user, of course. This feature just isn't ready yet.
        List<Technology> technologiesToSave = validTechnologyNames.stream()
                .map(t -> new Technology(t, project)).collect(toList());
        project.setTechnologies(technologiesToSave);

        projectRepository.save(project);

        Project lastSavedProject = projectRepository.findOne(10L);

        lastSavedProject.getTechnologies().stream()
                .map(Technology::getName).collect(toList())
                .containsAll(validTechnologyNames);
    }
}