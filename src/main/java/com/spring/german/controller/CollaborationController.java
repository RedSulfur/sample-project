package com.spring.german.controller;

import com.spring.german.entity.Project;
import com.spring.german.entity.Technology;
import com.spring.german.entity.User;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.repository.TechnologyRepository;
import com.spring.german.service.CollaborationService;
import com.spring.german.service.UserService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class CollaborationController {

    private static final Logger log = LoggerFactory.getLogger(CollaborationController.class);

    private CollaborationService collaborationService;
    private UserService userService;
    private ProjectRepository projectRepository;
    private TechnologyRepository technologyRepository;

    @Autowired
    public CollaborationController(CollaborationService collaborationService,
                                   UserService userService,
                                   ProjectRepository projectRepository,
                                   TechnologyRepository technologyRepository) {
        this.collaborationService = collaborationService;
        this.userService = userService;
        this.projectRepository = projectRepository;
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

        Set<String> technologies = collaborationService.getTechnologies(username, repoName);
        request.getSession().setAttribute("technologies", technologies);

        ModelAndView mav = this.getDefaultView();

        return mav;
    }

    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public ModelAndView publishProject(@ModelAttribute(value = "techs") String technologies,
                                       HttpServletRequest request, Principal principal) {

        log.info("Technologies set: {}", request.getSession().getAttribute("technologies"));

        Set<String> techs = (Set<String>) request.getSession().getAttribute("technologies");
        techs.forEach(t -> log.info("One of your techs: {}", t));

        String username = principal.getName();
        User user = userService.findBySso(username);
        log.info("User fetched by username(Collaboration controller): {}", user);



        /*HashSet<Technology> technologiesToSave = new HashSet<>();
        techs.forEach(t -> {
                log.info("T: {}", t);
                Technology saved = technologyRepository.save(new Technology(t));
                log.info("Technology saved: {}", saved);
                technologiesToSave.add(saved);
            }
        );*/

      /*
        String[] arrayToSave = techs.toArray(new String[techs.size()]);
        String username = principal.getName();
        User user = userService.findBySso(username);
        log.info("User fetched by username(Collaboration controller): {}", user);
        Project project = new Project();
        project.setTechnologies(arrayToSave);
        project.setUser(user);
        log.info("Project to be saved: {}", project);
        projectRepository.save(project);
      */
      request.getSession().removeAttribute("technologies");

        return this.getDefaultView();
    }

    private ModelAndView getDefaultView() {
        return new ModelAndView("collaboration");
    }
}