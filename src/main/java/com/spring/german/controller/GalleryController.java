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

@Controller
public class GalleryController {

    private static final Logger log = LoggerFactory.getLogger(GalleryController.class);
    public static final String GALLERY_VIEW = "gallery";

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
     * Fetches all the projects from database that have any of the technologies
     * provided by user, places these projects into the model and specifies a page
     * where these projects will be displayed.
     *
     * @param mav           object that is used to store projects and to specify
     *                      model name.
     * @param technologies  list that is used to search corresponding projects
     */
    @RequestMapping(value = "/gallery", method = RequestMethod.POST)
    public ModelAndView getProjects(ModelAndView mav,
                                    @ModelAttribute(value = "technologies") String technologies) {

        log.info("{}", technologies);

        List<Project> projects = projectService.getProjectsByTechnologyNames(technologies);

        GalleryControllerLogger.logAllTheExtractedProjects(projects);

        mav.setViewName(GALLERY_VIEW);
        mav.addObject("projects", projects);

        return mav;
    }

    private ModelAndView getDefaultModelAndView() {
        return new ModelAndView(GALLERY_VIEW);
    }

    /**
     * Provides helper methods for its outer class {@see GalleryController}
     */
    private static class GalleryControllerLogger {

        private static void logAllTheExtractedProjects(List<Project> projects) {
            projects.forEach(p -> log.info("Project fetched: {}", p.toString()));
        }
    }
}
