package com.spring.german.controller;

import com.spring.german.entity.Project;
import com.spring.german.service.interfaces.ProjectService;
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

import static com.spring.german.util.Endpoints.GALLERY_PAGE;

@Controller
public class GalleryController {

    private static final Logger log = LoggerFactory.getLogger(GalleryController.class);

    private ProjectService projectService;

    @Autowired
    public GalleryController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/gallery", method = RequestMethod.GET)
    public ModelAndView showGallery(Model model) {
        return this.getDefaultModelAndView();
    }

    /**
     * Takes care of displaying all the projects that have any technologies
     * specified by user
     *
     * @param technologies  list that is used to search corresponding projects
     */
    @RequestMapping(value = "/gallery", method = RequestMethod.POST)
    public ModelAndView getProjects(ModelAndView modelAndView,
                                    @ModelAttribute(value = "technologies") String technologies) {
        log.info("{}", technologies);

        List<Project> projects = projectService.getProjectsByTechnologyNames(technologies);

        GalleryControllerLogger.logAllTheExtractedProjects(projects);

        modelAndView = this.getDefaultModelAndView();
        modelAndView.addObject("projects", projects);

        return modelAndView;
    }

    private ModelAndView getDefaultModelAndView() {
        return new ModelAndView(GALLERY_PAGE);
    }

    private static class GalleryControllerLogger {

        private static void logAllTheExtractedProjects(List<Project> projects) {
            projects.forEach(p -> log.info("Project fetched: {}", p.toString()));
        }
    }
}
