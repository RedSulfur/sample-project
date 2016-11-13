package com.spring.german.controller;

import com.spring.german.service.CollaborationService;
import com.spring.german.service.interfaces.ProjectService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// TODO: Figure out how to get rid of @WebAppConfiguration
@RunWith(SpringRunner.class)
@WebMvcTest(CollaborationController.class)
@WebAppConfiguration
public class CollaborationControllerTest {

    public static final String VALID_GITHUB_USER = "RedSulfur";
    public static final String VALID_GITHUB_REPOSITORY = "sample-project";
    private List<String> validTechnologies;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockHttpSession session;

    @MockBean private ProjectService projectService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        validTechnologies = Arrays.asList("Travis Build,Spring Thymeleaf,Spring MVC,Spring validation,Gradle,Spring Security,Bootstrap"
                .split(","));
    }

    /**
     * As soon as an HttpServletRequest in {@link CollaborationController} is being passed
     * as method parameter it will tie this controller to be only runnable for classes
     * that are called from within an executing HttpServletRequest.
     *
     * That's why it is obligatory to use {@link MockHttpSession} object instead
     */
    @Test
    public void shouldReturnAllTechnologiesOnValidRepoName() throws Exception {

        mvc.perform(post("/collaborate").session(session)
                .with(user(VALID_GITHUB_USER)
                        .password("root")
                        .roles("ADMIN", "USER"))
                .contentType(APPLICATION_JSON)
                .param("repoName", VALID_GITHUB_REPOSITORY))
                .andExpect(view().name("collaboration"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowAnErrorOnNullRepoName() throws Exception {

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'values' must not be empty");
        mvc.perform(post("/collaborate")
                .with(user("RedSulfur")
                        .password("pass")
                        .roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("repoName", null));
    }

    @Test
    public void shouldSaveProjectAndClearSession() throws Exception {

        mvc.perform(get("/publish").session(session)
                .with(user("RedSulfur")
                        .password("pass")
                        .roles("USER", "ADMIN"))
                .contentType(APPLICATION_JSON)
                .sessionAttr("technologies", validTechnologies))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("collaboration"));

        verify(projectService, times(1))
            .saveProjectWithTechnologies(anyString(), anyObject());

        assertTrue(session.getValueNames().length == 0);

    }
}