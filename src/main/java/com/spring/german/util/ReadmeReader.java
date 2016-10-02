package com.spring.german.util;

import com.spring.german.exceptions.CustomException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Component
public class ReadmeReader {

    private static final Logger log = LoggerFactory.getLogger(ReadmeReader.class);

    public String readReadmeFromGithubRepository(String username, String repoName) {

        String body;
        try {
            URL url = new URL("https://raw.githubusercontent.com/" + username + "/" + repoName + "/master/README.md");
            log.info("Application is parsing readme from the following url: {}", url.toString());
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            body = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            throw new CustomException("There is no such user on github, or " +
                    "repository name you've specified is non existent", e);
        }

        return body;
    }
}
