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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;

import static com.spring.german.service.ProjectTestUtil.VALID_USERNAME;
import static com.spring.german.service.ProjectTestUtil.getValidTechnologies;
import static com.spring.german.service.ProjectTestUtil.getValidTechnologyNames;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultProjectServiceTest {

    private DefaultProjectService projectService;
    private User validUser;
    private List<Project> extractedProjects;

    @Rule public ExpectedException exception = ExpectedException.none();

    @Mock private ProjectRepository projectRepository;
    @Mock private UserService userService;

    @Before
    public void setUp() throws Exception {
        projectService = new DefaultProjectService(projectRepository, userService);
        extractedProjects = ProjectTestUtil.getListsOfProjects();
        validUser = ProjectTestUtil.getValidUser();
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

    @Test
    public void shouldSaveProjectWithTechnologies() {
        List<String> validTechnologies = getValidTechnologyNames();
        given(userService.getUserBySsoId(anyObject())).willReturn(validUser);

        projectService.saveProjectWithTechnologies(VALID_USERNAME, validTechnologies);

        verify(projectRepository, times(1)).save((Project) anyObject());
    }
}
