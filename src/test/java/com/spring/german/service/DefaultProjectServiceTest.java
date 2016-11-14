package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.exceptions.TechnologiesNotFoundException;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.spring.german.service.ProjectTestUtil.getListsOfProjects;
import static com.spring.german.service.ProjectTestUtil.getValidTechnologies;
import static com.spring.german.service.ProjectTestUtil.getValidUser;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultProjectServiceTest {

    public static final String VALID_USERNAME = "valid-username";

    private DefaultProjectService projectService;
    private User validUser;
    private List<Project> extractedProjects;

    @Rule public ExpectedException exception = ExpectedException.none();

    @Mock private ProjectRepository projectRepository;
    @Mock private UserService userService;

    @Before
    public void setUp() throws Exception {
        projectService = new DefaultProjectService(projectRepository, userService);
        extractedProjects = getListsOfProjects();
        validUser = getValidUser();
    }

    @Test
    public void shouldSaveProjectWithTechnologies() {
        List<String> validTechnologies = getValidTechnologies();
        given(userService.getUserBySsoId(anyObject())).willReturn(validUser);

        projectService.saveProjectWithTechnologies(VALID_USERNAME, validTechnologies);

        verify(projectRepository, times(1)).save((Project) anyObject());
    }

    @Test
    public void shouldReturnProjectsThatCorrespondToTheRequestedTechnologyNames() {
        when(projectRepository.findDistinctByTechnologiesNameIn(anyObject()))
                .thenReturn(extractedProjects);

        List<Project> projects = projectService.getProjectsByTechnologyNames("Maven");

        projects.forEach(project -> assertThat(project.getTechnologies()
                .stream().map(Technology::getName).collect(Collectors.toList()), hasItem("Maven")));
    }

    @Test
    public void shouldThrowAnErrorIfNoTechnologiesWereSpecified() {
        exception.expect(TechnologiesNotFoundException.class);
        exception.expectMessage("You specified no technologies to search by");
        projectService.getProjectsByTechnologyNames(null);
    }
}
