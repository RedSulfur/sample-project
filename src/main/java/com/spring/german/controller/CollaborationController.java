package com.spring.german.controller;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class CollaborationController {

    private static final Logger log = LoggerFactory.getLogger(GalleryController.class);

    private static final String REGEX = "\\[([a-zA-z ]*)\\]\\(.+\\)";

    /**
     * When issued by a respective get method performs a redirection
     * to the template called collaboration.html
     *
     * @param mav {@link ModelAndView} object that specifies subsequent
     *            view
     * @return    {@link ModelAndView} object
     */
    @RequestMapping(value = "/collaborate", method = RequestMethod.GET)
    public ModelAndView getRepoName(ModelAndView mav) {
        mav.setViewName("collaboration");
        return mav;
    }

    /**
     * Searches for the readme file at user's GitHub repository
     * and establishes a url connection to it. After connection
     * to file was acquired controller performs its processing
     * in order to obtain the name of every technology that was
     * used to create the given repository.
     *
     * @param repoName  name of the repository to be processed
     * @param mav       {@link ModelAndView} object contains all
     *                  the information for the further workflow
     * @param principal {@link Principal} object is needed to determine
     *                  a username of the current user
     *
     * @return  {@link ModelAndView} object that contains a view
     *          name and a list of technologies
     * @throws IOException
     */
    @RequestMapping(value = "/collaborate", method = RequestMethod.POST)
    public ModelAndView getRepoData(@ModelAttribute(value = "repoName") String repoName,
                              ModelAndView mav, Principal principal) throws IOException {
        Assert.notNull(repoName);
        log.info("Repository name fetched: {}", repoName);
        log.info("Current logged in user: {}", principal.getName());

        URL url = new URL("https://raw.githubusercontent.com/RedSulfur/" + repoName + "/master/README.md");
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        String body = IOUtils.toString(in, encoding);

        List<String> technologies = new ArrayList<>();
        Matcher m = Pattern.compile(REGEX).matcher(body);
        while (m.find()) {
            technologies.add(m.group(1));
        }

        for (String data : technologies) {
            log.info(data);
        }

        mav.setViewName("collaboration");
        mav.addObject("technologies", technologies);

        return mav;
    }
}
