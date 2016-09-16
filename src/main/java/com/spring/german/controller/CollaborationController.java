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

    Logger log = LoggerFactory.getLogger(GalleryController.class);
    private static final String REGEX = "\\[([a-zA-z ]*)\\]\\(.+\\)";

    @RequestMapping(value = "/collaborate", method = RequestMethod.GET)
    public ModelAndView getRepoName(ModelAndView mav) {
        mav.setViewName("collaboration");
        return mav;
    }

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

        log.info("*********************************8");
        for (String data : technologies) {
            log.info(data);
        }

        mav.setViewName("collaboration");
        mav.addObject("technologies", technologies);

        return mav;
    }
}
