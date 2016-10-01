package com.spring.german.controller;

import com.spring.german.entity.Project;
import com.spring.german.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class GalleryController {

    private static final Logger log = LoggerFactory.getLogger(GalleryController.class);

    private ProjectService projectService;

    @Autowired
    public GalleryController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public String showGallery(Model model) {
        return "gallery";
    }

    @RequestMapping(value = "/gallery", method = RequestMethod.POST)
    public ModelAndView getProjects(ModelAndView mav,
                                    @ModelAttribute(value = "technologies") String technologies) {

        log.info("{}", technologies);

        List<Project> projects = projectService.findByTechnologyNames(technologies);

        projects.forEach(p -> log.info("Project fetched: {}", p.toString()));

        mav.addObject("projects", projects);
        mav.setViewName("gallery");

        return mav;
    }
}
