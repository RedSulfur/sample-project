package com.spring.german.service;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.exceptions.TechnologiesNotFoundException;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.UserService;
import com.spring.german.util.TestUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.german.util.TestUtil.VALID_USERNAME;
import static com.spring.german.util.TestUtil.getValidTechnologyNames;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
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
        extractedProjects = TestUtil.getListsOfProjects();
        validUser = TestUtil.getValidUser();
    }

    @Test
    public void shouldReturnProjectsThatCorrespondToTheRequestedTechnologyNames() {
        // given
        when(projectRepository.findDistinctByTechnologiesNameIn(anyObject()))
                .thenReturn(extractedProjects);

        // when
        List<Project> projects = projectService.getProjectsByTechnologyNames("Maven");

        // then
        verify(projectRepository, atLeastOnce())
                .findDistinctByTechnologiesNameIn(Collections.singletonList("Maven"));

        projects.forEach(project -> assertThat(project.getTechnologies()
                .stream().map(Technology::getName).collect(Collectors.toList()), hasItem("Maven")));
    }

    @Test
    public void shouldThrowAnErrorIfNoTechnologiesWereSpecified() {
        // given
        exception.expect(TechnologiesNotFoundException.class);
        exception.expectMessage("You specified no technologies to search by");

        // when
        projectService.getProjectsByTechnologyNames(null);

        // then
        verify(projectRepository, never())
                .findDistinctByTechnologiesNameIn(anyObject());
    }

    @Test
    public void shouldSaveProjectWithTechnologies() {
        List<String> validTechnologies = getValidTechnologyNames();
        given(userService.getUserBySsoId(anyObject())).willReturn(validUser);

        projectService.saveProjectWithTechnologies(VALID_USERNAME, validTechnologies);

        verify(projectRepository, times(1)).save((Project) anyObject());
    }
}
