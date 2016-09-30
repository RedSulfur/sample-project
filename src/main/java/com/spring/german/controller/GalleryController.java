package com.spring.german.controller;

import com.spring.german.entity.Project;
import com.spring.german.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class GalleryController {

    Logger log = LoggerFactory.getLogger(GalleryController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(value = "/gallery", method = RequestMethod.POST)
    public ModelAndView getProjects(ModelAndView mav, @ModelAttribute(value = "technologies") String technologies) {

        log.info("In GalleryController");
        log.info("{}", technologies);

        List<Project> projects = projectRepository.findDistinctByTechnologiesNameIn(Arrays.asList(technologies));

        projects.forEach(p -> log.info("Project fetched: {}", p.toString()));

        mav.addObject("projects", projects);
        mav.setViewName("gallery");

        return mav;
    }

    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public String showGallery(Model model) {


        return "gallery";
    }
}
