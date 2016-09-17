package com.spring.german.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CollaborationController.class)
public class CollaborationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldReturnAllTechnologiesOnValidRepoName() throws Exception {
        mvc.perform(post("/collaborate")
                .with(user("sulfur")
                        .password("root")
                        .roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("repoName", "sample-project"))
                .andExpect(view().name("collaboration"))
                .andExpect(model().attribute("technologies", hasToString(equalTo("[Travis Build, Spring Thymeleaf, Spring MVC, Spring validation, Gradle, Spring Security, Bootstrap]"))))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowAnErrorOnNullRepoName() throws Exception {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("'values' must not be empty");
        mvc.perform(post("/collaborate")
                .with(user("sulfur")
                        .password("root")
                        .roles("ADMIN", "USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("repoName", null));
    }
}