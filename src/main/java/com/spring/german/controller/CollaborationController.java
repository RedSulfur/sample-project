package com.spring.german.controller;

import com.spring.german.exceptions.EmptyRepositoryNameException;
import com.spring.german.service.CollaborationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static java.util.Optional.of;

@Controller
public class CollaborationController {

    private CollaborationService collaborationService;

    @Autowired
    public CollaborationController(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    @RequestMapping(value = "/collaborate", method = RequestMethod.GET)
    public ModelAndView displayCollaborationPage() {
        return this.getDefaultView();
    }

    /**
     * Enables requested technologies to be displayed at corresponding view
     *
     * @param repoName  name of the repository to be processed
     * @param principal {@link Principal} object is needed to determine
     *                  a username of currently logged in user
     */
    @RequestMapping(value = "/collaborate", method = RequestMethod.POST)
    public ModelAndView getTechnologiesByRepoName(@ModelAttribute(value = "repoName") String repoName,
                                              HttpServletRequest request, Principal principal) {

        String notEmptyRepoName = getRepoNameIfNotNull(repoName);

        CollaborationControllerLogger.logFetchedRepositoryName(notEmptyRepoName);
        String userName = principal.getName();
        CollaborationControllerLogger.logCurrentlyLoggedInUser(userName);

        populateSessionWithTechnologies(userName, notEmptyRepoName, request);

        return this.getDefaultView();
    }

    private String getRepoNameIfNotNull(String repoName) {

        return of(repoName).orElseThrow(() ->
                new EmptyRepositoryNameException("You have not provided any repository name"));

    }

    /**
     *
     * @param userName
     * @param notEmptyRepoName
     * @param request
     */
    private void populateSessionWithTechnologies(String userName,
                                                 String notEmptyRepoName,
                                                 HttpServletRequest request) {
        List<String> technologies =
                collaborationService.getReadmeFromGithubRepositoy(userName, notEmptyRepoName);
        CollaborationControllerLogger.logAllTheExtractedTechnologies(technologies);
        request.getSession().setAttribute("technologies", technologies);
    }

    /**
     * Gets all the technology names passed as a session attribute.
     * Defines a name of the currently logged in user which is used
     * to persist project into the database. Saves a new Project
     * associated with obtained technologies.
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
    public ModelAndView publishProject(HttpServletRequest request, Principal principal) {

        List<String> technologies = (List<String>) request.getSession().getAttribute("technologies");
        CollaborationControllerLogger.logTechnologiesObtainedFromRequest(technologies);
        String username = principal.getName();
        collaborationService.saveProjectWithTechnologies(username, technologies);
        request.getSession().removeAttribute("technologies");

        return this.getDefaultView();
    }

    /**
     * Supplies all the  with the default view object.
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