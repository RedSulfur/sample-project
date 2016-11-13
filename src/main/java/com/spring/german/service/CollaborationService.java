package com.spring.german.service;

import com.spring.german.exceptions.EmptyRepositoryNameException;
import com.spring.german.exceptions.ReadmeNotFoundException;
import com.spring.german.repository.ProjectRepository;
import com.spring.german.service.interfaces.UserService;
import com.spring.german.util.GitHubRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static java.util.Optional.of;
import static java.util.regex.Pattern.compile;

@Service
public class CollaborationService {

    private ProjectRepository projectRepository;
    private UserService userService;

    // e.g. "[Spring Thymeleaf](http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html)"
    // matches "Spring Thymeleaf"
    private static final String TECHNOLOGY_NAME_BY_GITHUB_README_REFERENCE = "\\[([a-zA-z ]*)\\]\\(.+\\)"; //TODO: Is it a correct place for this variable? Isn't the name too long?

    @Autowired
    public CollaborationService(ProjectRepository projectRepository,
                                UserService userService) {
        this.userService = userService;
        this.projectRepository = projectRepository;
    }

    public GitHubRepository getGitHubRepositoryObject(String repoName, String userName) {

        String notEmptyRepoName = this.getNotEmptyRepoName(repoName);
        return new GitHubRepository(notEmptyRepoName, userName);
    }

    private String getNotEmptyRepoName(String repoName) {
        return of(repoName).orElseThrow(() ->
                new EmptyRepositoryNameException("You have not provided any repository name"));
    }

    public void populateSessionWithTechnologiesFromRepo(HttpSession session,
                                                        GitHubRepository gitHubRepository) {

        String readmeBody = this.getReadmeFromGitHubRepository(gitHubRepository);
        List<String> technologies = this.extractTechnologyNamesFromReadmeBody(readmeBody);
        session.setAttribute("technologies", technologies);
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
        Matcher m = compile(TECHNOLOGY_NAME_BY_GITHUB_README_REFERENCE).matcher(body);
        while (m.find()) {
            technologies.add(m.group(1));
        }

        return technologies;
    }
}