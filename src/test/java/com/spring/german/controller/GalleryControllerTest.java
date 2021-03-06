package com.spring.german.controller;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.service.interfaces.ProjectService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.german.util.Endpoints.GALLERY_PAGE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(GalleryController.class)
public class GalleryControllerTest {

    private List<Project> projects;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProjectService projectService;

    @Before
    public void setup() {
        List<Technology> validTechnologies = Arrays.stream(("Travis Build,Spring Thymeleaf,Spring MVC,Spring validation," +
                "Gradle,Spring Security,Bootstrap")
                .split(",")).map(Technology::new)
                .collect(Collectors.toList());
        projects = Collections.singletonList(new Project("default", validTechnologies, null));
    }

    @Test
    public void showGalleryIfUserHasAdminCredentials()
            throws Exception {

        mvc.perform(get("/gallery")
                .with(user("admin")
                        .password("pass")
                        .roles("USER","ADMIN")))
                .andExpect(status().isOk())

                .andExpect(redirectedUrl(null))
                .andExpect(view().name(GALLERY_PAGE));
    }

    @Test
    public void redirectToLoginPageIfNotAuthorized()
            throws Exception {

        mvc.perform(get("/gallery"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFindAllTheProjectsByStringOfTechnologies()
            throws Exception {

        given(projectService.getProjectsByTechnologyNames(anyString()))
                .willReturn(projects);

        mvc.perform(post("/gallery")
                .with(user("RedSulfur")
                        .password("pass")
                        .roles("USER", "ADMIN"))
                        .contentType(APPLICATION_JSON)
                        .param("technologies", anyString()))
                .andExpect(status().isOk())
                .andExpect(view().name(GALLERY_PAGE))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("projects", hasSize(1)));

        verify(projectService, times(1)).getProjectsByTechnologyNames(anyString());
    }
}