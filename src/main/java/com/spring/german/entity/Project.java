package com.spring.german.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String logo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "projects_technologies",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "technology_id")})
    private Set<Technology> technologies = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Project(String logo, Set<Technology> technologies, User user) {
        this.logo = logo;
        this.technologies = technologies;
        this.user = user;
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
