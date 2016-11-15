package com.spring.german.controller;

import com.spring.german.service.CollaborationService;
import com.spring.german.service.interfaces.ProjectService;
import com.spring.german.util.GitHubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class CollaborationController {

    private static final Logger log = LoggerFactory.getLogger(CollaborationController.class);

    private CollaborationService collaborationService;
    private ProjectService projectService;

    @Autowired
    public CollaborationController(CollaborationService collaborationService,
                                   ProjectService projectService) {
        this.collaborationService = collaborationService;
        this.projectService = projectService;
    }

    @RequestMapping(value = "/collaborate", method = RequestMethod.GET)
    public ModelAndView displayCollaborationPage() {
        return this.getDefaultView();
    }

    /**
     * Enables requested technologies to be displayed at corresponding view
     *
     * @param repoName  name of the repository to be processed
     * @param principal object that is needed to determine a username of
     *                  currently logged in user
     */
    @RequestMapping(value = "/collaborate", method = RequestMethod.POST)
    public ModelAndView getTechnologiesByRepoName(@ModelAttribute(value = "repoName") String repoName,
                                              HttpServletRequest request, Principal principal) {
        String userName = principal.getName();
        CollaborationControllerLogger.logCurrentlyLoggedInUser(userName);
        GitHubRepository gitHubRepository = new GitHubRepository(repoName, userName);
        HttpSession session = request.getSession();
        collaborationService.populateSessionWithTechnologiesFromRepo(session, gitHubRepository);

        return this.getDefaultView();
    }

    /**
     * Saves a new project associated with the current user and all the
     * technologies that he has defined
     *
     * @param request   an object that is used to obtain technology names from
     *                  the session
     * @param principal object that is needed to determine a username of
     *                  currently logged in user
     */
    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public ModelAndView persistProject(HttpServletRequest request, Principal principal) {
        List<String> technologies = (List<String>) request.getSession().getAttribute("technologies");
        CollaborationControllerLogger.logTechnologiesObtainedFromRequest(technologies);
        String username = principal.getName();
        projectService.saveProjectWithTechnologies(username, technologies);
        request.getSession().removeAttribute("technologies");

        return this.getDefaultView();
    }

    private ModelAndView getDefaultView() {
        return new ModelAndView("collaboration");
    }

    private static class CollaborationControllerLogger {

        private static final Logger log = LoggerFactory.getLogger(CollaborationController.class);

        private static void logCurrentlyLoggedInUser(String username) {
            log.info("Currently logged in user: {}", username);
        }

        private static void logTechnologiesObtainedFromRequest(List<String> technologies) {
            log.info("Technologies list obtained from collaboration.html: {}", technologies);
        }
    }
}