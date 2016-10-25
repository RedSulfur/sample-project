package com.spring.german.controller;

import com.spring.german.service.CollaborationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class CollaborationController {

    private static final Logger log = LoggerFactory.getLogger(CollaborationController.class);

    private CollaborationService collaborationService;

    @Autowired
    public CollaborationController(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    @RequestMapping(value = "/collaborate", method = RequestMethod.GET)
    public ModelAndView getRepoName() {
        return this.getDefaultView();
    }

    /**
     * @param repoName  name of the repository to be processed
     * @param principal {@link Principal} object is needed to determine
     *                  a username of the current user
     *
     * @return  {@link ModelAndView} object that contains a view
     *          name and a list of technologies
     * @throws IOException
     */
    @RequestMapping(value = "/collaborate", method = RequestMethod.POST)
    public ModelAndView getTechnologiesByRepo(@ModelAttribute(value = "repoName") String repoName,
                                              HttpServletRequest request, Principal principal) {
        Assert.notNull(repoName);

        CollaborationControllerLogger.logFetchedRepositoryName(repoName);
        String userName = principal.getName();
        CollaborationControllerLogger.logCurrentlyLoggedInUser(userName);
        List<String> technologies =
                collaborationService.getTechnologiesFromGithubRepositoy(userName, repoName);
        CollaborationControllerLogger.logAllTheExtractedTechnologies(technologies);
        request.getSession().setAttribute("technologies", technologies);

        return this.getDefaultView();
    }

    /**
     * Gets all the technology names passed as a session attribute.
     * Defines a name of the currently logged in user which is used
     * to persist project into the database.
     * When finishing its work method clears the session.
     *
     * @param request   an object that is used to obtain technology names from
     *                  the session
     * @param principal {@link Principal} object is needed to determine
     *                  a username of the current user
     * @return          {@link ModelAndView} object that contains no model attributes and
     *                  a default view name.
     */
    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public String publishProject(HttpServletRequest request, Principal principal) {

        List<String> technologies = (List<String>) request.getSession().getAttribute("technologies");
        CollaborationControllerLogger.logTechnologiesObtainedFromRequest(technologies);
        String username = principal.getName();
        collaborationService.saveProjectWithTechnologies(username, technologies);
        request.getSession().removeAttribute("technologies");

        return "collaboration";
    }

    /**
     *
     * Supplies all the methods from {@link CollaborationController} with
     * the default view object. It is used to redirect to a default resource
     * when controller's method finishes its work.
     *
     * @return {@link ModelAndView} object that contains a default view name for
     *                              the given controller
     */
    private ModelAndView getDefaultView() {
        return new ModelAndView("collaboration");
    }

    /**
     * Provides logging methods for its outer class {@see CollaborationController}
     */
    private static class CollaborationControllerLogger {

        private static final Logger log = LoggerFactory.getLogger(CollaborationController.class);

        private static void logFetchedRepositoryName(String repoName) {
            log.info("Repository name fetched: {}", repoName);
        }

        private static void logCurrentlyLoggedInUser(String username) {
            log.info("Currently logged in user: {}", username);
        }

        private static void logAllTheExtractedTechnologies(List<String> technologies) {
            log.info("Extracted technologies: {}", technologies);
        }

        public static void logTechnologiesObtainedFromRequest(List<String> technologies) {
            log.info("Technologies list obtained from collaboration.html: {}", technologies);
        }
    }
}