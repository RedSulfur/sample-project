package com.spring.german.controller;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.spring.german.entity.Technology;
import com.spring.german.service.CollaborationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.constraints.AssertTrue;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CollaborationController.class)
@WebAppConfiguration
public class CollaborationControllerTest {

    private static final String VALID_GITHUB_USER = "RedSulfur";
    private static final String VALID_GITHUB_REPOSITORY = "sample-project";
    private List<String> validTechnologies;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockHttpSession session;

    @MockBean private CollaborationService collaborationService;

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

        given(collaborationService.getTechnologies(VALID_GITHUB_USER, VALID_GITHUB_REPOSITORY))
                .willReturn(validTechnologies);

        mvc.perform(post("/collaborate").session(session)
                .with(user(VALID_GITHUB_USER)
                        .password("root")
                        .roles("ADMIN", "USER"))
                .contentType(APPLICATION_JSON)
                .param("repoName", VALID_GITHUB_REPOSITORY))
                .andExpect(view().name("collaboration"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk());

        assertTrue(((List<String>) session.getAttribute("technologies"))
                .containsAll(validTechnologies));
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

        verify(collaborationService, times(1))
            .saveProjectWithTechnologies(anyString(), anyObject());

        assertTrue(session.getValueNames().length == 0);

    }
}