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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@RestController
public class CollaborationController {

    Logger log = LoggerFactory.getLogger(GalleryController.class);

    @RequestMapping(value = "/collaborate", method = RequestMethod.GET)
    public ModelAndView getRepoName(ModelAndView mav) {
        mav.setViewName("collaboration");
        return mav;
    }

    @RequestMapping(value = "/collaborate", method = RequestMethod.POST)
    public String processRepoReadme(@ModelAttribute(value = "repoName") String repoName,
                                    ModelAndView mav) throws IOException, URISyntaxException {
        Assert.notNull(repoName);
        log.info("Repository name fetched: {}", repoName);

        URL url = new URL("https://raw.githubusercontent.com/RedSulfur/" + repoName + "/master/README.md");
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();  // ** WRONG: should use "con.getContentType()" instead but it returns something like "text/html; charset=UTF-8" so this value must be parsed to extract the actual encoding
        encoding = encoding == null ? "UTF-8" : encoding;
        String body = IOUtils.toString(in, encoding);
        log.info("****************************************");
        log.info(body);

        return "";
    }

    public Stream<String> getLineReader(String repoName)
            throws IOException, URISyntaxException {
         return Files.lines(Paths.get(new URI("https://raw.githubusercontent.com/RedSulfur/", repoName ,"/master/README.md")));
    }
}
