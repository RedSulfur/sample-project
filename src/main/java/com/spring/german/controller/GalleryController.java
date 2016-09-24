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

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GalleryController {

    Logger log = LoggerFactory.getLogger(GalleryController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public String getProjects(Model model) {

        /*projectRepository.findByTechnologiesContaining("Maven")
                .stream().map(Project::getTechnologies)
                .collect(Collectors.toList()).forEach(p -> list.add(p.split(",")));*/

        List<Project> projects = projectRepository.getProjectsWithSpecificTechnologies();

        projects.forEach(project -> log.info("Technology: {}", project.getTechnologies()));

        model.addAttribute("projects", projects);

        return "gallery";
    }
}
