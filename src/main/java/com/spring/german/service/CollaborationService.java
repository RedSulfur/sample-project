package com.spring.german.service;

import com.spring.german.exceptions.ReadmeNotFoundException;
import com.spring.german.exceptions.RepositoryNotSpecifiedException;
import com.spring.german.util.GitHubRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

@Service
public class CollaborationService {

    // e.g. "[Spring Thymeleaf](http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html)"
    // matches "Spring Thymeleaf"
    private static final String TECHNOLOGY_NAME_BY_GITHUB_README_REFERENCE = "\\[([a-zA-z ]*)\\]\\(.+\\)"; //TODO: Is it a correct place for this variable? Isn't the name too long?

    public void populateSessionWithTechnologiesFromRepo(HttpSession session,
                                                        GitHubRepository gitHubRepository) {
        GitHubRepository notEmptyGitHubRepository = this.getNotEmptyGithubRepository(gitHubRepository);
        String readmeBody = this.getReadmeFromGitHubRepository(notEmptyGitHubRepository);
        List<String> technologies = this.extractTechnologyNamesFromReadmeBody(readmeBody);
        session.setAttribute("technologies", technologies);
    }

    private GitHubRepository getNotEmptyGithubRepository(GitHubRepository gitHubRepository) {
        return Optional.ofNullable(gitHubRepository)
                .orElseThrow(() -> new RepositoryNotSpecifiedException("You have to specify repository name"));
    }

    private String getReadmeFromGitHubRepository(GitHubRepository gitHubRepository) {

        String readmeBody;
        try {
            URL url = new URL("https://raw.githubusercontent.com/"
                    + gitHubRepository.getOwnerName() + "/"
                    + gitHubRepository.getRepoName() + "/master/README.md");
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            readmeBody = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            throw new ReadmeNotFoundException("There is no such user on github, or " +
                    "repository name you've specified is non existent", e);
        }

        return readmeBody;
    }

    private List<String> extractTechnologyNamesFromReadmeBody(String body) {

        List<String> technologies = new ArrayList<>();
        Matcher matcher = compile(TECHNOLOGY_NAME_BY_GITHUB_README_REFERENCE).matcher(body);
        while (matcher.find()) {
            technologies.add(matcher.group(1));
        }

        return technologies;
    }
}