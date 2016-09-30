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
import java.io.*;
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

    /**
     * When issued by a respective get method renders
     * a template called collaboration.html
     *
     * @return a respective template name
     */
    @RequestMapping(value = "/collaborate", method = RequestMethod.GET)
    public ModelAndView getRepoName() {
        return this.getDefaultView();
    }

    /**
     * Searches for the readme file at user's GitHub repository
     * and establishes a url connection to it. After connection
     * to file was acquired, controller performs its processing
     * in order to obtain the name of every technology that was
     * used to create the given repository.
     *
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

        log.info("Repository name fetched: {}", repoName);
        String username = principal.getName();
        log.info("Currently logged in user: {}", username);

        List<String> technologies = collaborationService.getTechnologies(username, repoName);
        request.getSession().setAttribute("technologies", technologies);

        return this.getDefaultView();
    }

    /**
     * On accessing the /publish endpoint, method gets all the technology
     * names that were passed to it as a session attribute. Defines a
     * name of the currently logged in user and maps a list of strings
     * that represent technologies to the list of the corresponding objects.
     * These {@link com.spring.german.entity.Technology} objects are being
     * associated with the obtained user and his project. This entwined
     * chain is being saved afterwards. When finishing its work method clears
     * the session.
     *
     * @param  request   an object that is used to obtain technology names from
     *                   the session
     * @param  principal {@link Principal} object is needed to determine
     *                   a username of the current user
     * @return {@link ModelAndView} object that contains no model attributes and
     *         a default view name.
     */
    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public ModelAndView publishProject(HttpServletRequest request, Principal principal) {

        log.info("Technologies list obtained from collaboration.html: {}",
                request.getSession().getAttribute("technologies"));

        List<String> technologies = (List<String>) request.getSession().getAttribute("technologies");

        String username = principal.getName();

        collaborationService.saveProjectWithTechnologies(username, technologies);

        request.getSession().removeAttribute("technologies");

        return this.getDefaultView();
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
}