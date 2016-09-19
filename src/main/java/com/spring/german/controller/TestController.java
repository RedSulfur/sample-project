package com.spring.german.controller;

import com.spring.german.entity.Project;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.repository.RestaurantsRepository;
import com.spring.german.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class TestController {

    Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "/restaurants", method = RequestMethod.GET)
    public String getRestaurants(Model model) {

//        List<Project> projects = projectRepository.getProjectsWithSpecificTechnologies();
//        model.addAttribute("restaurants", projects);
        List<Project> projects = projectRepository.findByTechnologiesContaining("Maven");

        for (Project project:
             projects) {
            log.info("Technologies fetched: {}", project.getTechnologies());
        }
        model.addAttribute("projects", projects);
        return "test";
    }
}
