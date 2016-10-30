package com.spring.german.entity;

import com.spring.german.exceptions.ReadmeNotFound;
import org.apache.commons.io.IOUtils;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String logo;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Technology> technologies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Project() {
    }

    public Project(String logo, User user) {
        this.logo = logo;
        this.user = user;
    }

    public Project(String logo, List<Technology> technologies, User user) {
        this.logo = logo;
        this.technologies = technologies;
        this.user = user;
    }

    //TODO: correct place for it?
    /**
     * Searches for the readme file at user's GitHub repository
     * and establishes a url connection to it. After connection
     * to the file was acquired, method performs its processing
     * in order to obtain a string representation of the fetched
     * data.
     */
    public static String getReadmeFromGithubRepository(String userName, String repoName) {

        String readmeBody;
        try {
            URL url = new URL("https://raw.githubusercontent.com/"
                    + userName + "/" + repoName + "/master/README.md");
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            readmeBody = IOUtils.toString(in, encoding);
        } catch (IOException e) {
            throw new ReadmeNotFound("There is no such user on github, or " +
                    "repository name you've specified is non existent", e);
        }

        return readmeBody;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<Technology> technologies) {
        this.technologies = technologies;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", technologies='" + technologies + '\'' +
                ", logo='" + logo + '\'' +
                ", user=" + user +
                '}';
    }
}
