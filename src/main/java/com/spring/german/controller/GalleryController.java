package com.spring.german.controller;

import com.spring.german.entity.Project;
import com.spring.german.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GalleryController {

    Logger log = LoggerFactory.getLogger(GalleryController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public String getProjects(Model model) {

        List<Project> projects = projectRepository.getProjectsWithSpecificTechnologies();

        projects.forEach(project -> log.info("Technology: {}", Arrays.toString(project.getTechnologies())));

        model.addAttribute("projects", projects);

        return "gallery";
    }
}
