package com.spring.german.controller;


import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.repository.TechnologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    private ProjectRepository projectRepository;

    private final Logger log = LoggerFactory.getLogger(GalleryController.class);

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String testOneToManyBinding() {

        log.info("In TestController:");

        List<Project> projects = projectRepository
                .findDistinctByTechnologiesNameIn(Arrays.asList("Git", "Ant"));

        projects.forEach(p -> log.info("Technology: {}", p.toString()));

        return "login";
    }
}
