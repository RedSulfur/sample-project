package com.spring.german.service;

import com.spring.german.exceptions.CustomException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CollaborationService {

    private static final String REGEX = "\\[([a-zA-z ]*)\\]\\(.+\\)";

    private static final Logger log = LoggerFactory.getLogger(CollaborationService.class);

    public Set<String> getTechnologies(String username, String repoName) {

        String body;
        try {
            URL url = new URL("https://raw.githubusercontent.com/" + username + "/" + repoName + "/master/README.md");
            log.info("Application will parse readme from the following url: {}", url.toString());
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            body = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            throw new CustomException("There is no such user on github, or " +
                    "repository name you've specified is non existent", e);
        }
        Set<String> technologies = new HashSet<>();
        Matcher m = Pattern.compile(REGEX).matcher(body);
        while (m.find()) {
            technologies.add(m.group(1));
        }
        return technologies;
    }
}
