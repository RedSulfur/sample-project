package com.spring.german.controller;

import com.spring.german.repository.FilmRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GalleryController.class)
public class GalleryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private FilmRepository filmRepository;

    @Test
    public void showGalleryIfUserHasAdminCredentials() throws Exception {
        mvc.perform(get("/gallery")
                .with(user("admin")
                        .password("pass")
                        .roles("USER","ADMIN")))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl(null))
                .andExpect(view().name("gallery"));
    }
}