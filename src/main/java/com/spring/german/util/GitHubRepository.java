package com.spring.german.util;

import com.spring.german.exceptions.ReadmeNotFoundException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GitHubRepository {

    private final String repoName;
    private final String ownerName;

    public GitHubRepository(String repoName, String ownerName) {
        this.repoName = repoName;
        this.ownerName = ownerName;
    }

    public String getReadmeAsString() {
        String body;
        try {
            URL url = this.getUrlToReadme();
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            body = IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new ReadmeNotFoundException("There is no such user on github, or " +
                    "repository name you've specified is non existent");
        }
        return body;
    }

    private URL getUrlToReadme() throws MalformedURLException {
        return new URL("https://raw.githubusercontent.com/"
                + ownerName + "/"
                + repoName + "/master/README.md");
    }

    @Override
    public String toString() {
        return "GitHubRepository{" +
                "repoName='" + repoName + '\'' +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }
}
