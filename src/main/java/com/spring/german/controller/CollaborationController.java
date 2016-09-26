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
     * to file was acquired controller performs its processing
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
    public ModelAndView getRepoData(@ModelAttribute(value = "repoName") String repoName,
                                    HttpServletRequest request, Principal principal) {
        Assert.notNull(repoName);

        log.info("Repository name fetched: {}", repoName);
        String username = principal.getName();
        log.info("Currently logged in user: {}", username);

        List<String> technologies = collaborationService.getTechnologies(username, repoName);
        request.getSession().setAttribute("technologies", technologies);

        ModelAndView mav = this.getDefaultView();

        return mav;
    }

    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public ModelAndView publishProject(@ModelAttribute(value = "techs") String technologies,
                                       HttpServletRequest request) {

        log.info("Parameter is not an array: {}", request.getSession().getAttribute("technologies"));

        List<String> techs = (List<String>) request.getSession().getAttribute("technologies");
        techs.forEach(t -> log.info("One of your techs: {}", t));

        request.getSession().removeAttribute("technologies");

        return this.getDefaultView();
    }

    private ModelAndView getDefaultView() {
        return new ModelAndView("collaboration");
    }
}