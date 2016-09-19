package com.spring.german.service;

import com.spring.german.controller.GalleryController;
import com.spring.german.exceptions.CustomException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CollaborationService {

    private static final String REGEX = "\\[([a-zA-z ]*)\\]\\(.+\\)";

    private static final Logger log = LoggerFactory.getLogger(CollaborationService.class);

    public List<String> getTechnologies(String repoName) {

        String body;
        try {
            URL url = new URL("https://raw.githubusercontent.com/RedSulfur/" + repoName + "/master/README.md");
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            body = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            throw new CustomException("Url corrupted contaminated", e);
        }
        List<String> technologies = new ArrayList<>();
        Matcher m = Pattern.compile(REGEX).matcher(body);
        while (m.find()) {
            technologies.add(m.group(1));
        }

        return technologies;
    }

}
