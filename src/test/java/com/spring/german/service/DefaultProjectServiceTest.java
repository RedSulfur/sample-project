package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.repository.ProjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultProjectServiceTest {

    private DefaultProjectService projectService;
    private Project validProject;
    private List<Project> extractedProjects;

    @Mock private ProjectRepository projectRepository;

    @Before
    public void setUp() throws Exception {
        projectService = new DefaultProjectService(null, null);
        validProject = new Project("test-logo", new User());
        extractedProjects = getListsOfProjects();
    }

    private List<Project> getListsOfProjects() {

        List<Technology> technologies = Stream.of("Travis Build", "Maven", "Maven", "Spring validation",
                "Gradle", "Maven", "Maven")
                .map(Technology::new).collect(toList());

        return this.splitInChunks(technologies, 2).stream()
                .map(l -> new Project("test-logo", l, new User()))
                .collect(Collectors.toList());
    }

    private <T> List<List<T>> splitInChunks(List<T> listToSplit, int length) {
        int listSize = listToSplit.size();
        int fullChunks = (listSize - 1) / length;
        return IntStream.range(0, fullChunks + 1)
                .mapToObj(n -> listToSplit.subList(n * length, n == fullChunks ? listSize : (n + 1) * length))
                .collect(toList());
    }

    @Test
    public void shouldSaveProject() {
        when(projectRepository.save((Project) anyObject()))
                .then(returnsFirstArg());

        assertThat(projectService.save(validProject), is(validProject));
    }

    @Test
    public void shouldReturnProjectsCorrespondingToTheInput() {
        when(projectRepository.findDistinctByTechnologiesNameIn(anyObject()))
                .thenReturn(extractedProjects);

        List<Project> projects = projectService.getProjectsByTechnologyNames("Maven");

        projects.forEach(project -> assertThat(project.getTechnologies()
                .stream().map(Technology::getName).collect(Collectors.toList()), hasItem("Maven")));
    }
}
