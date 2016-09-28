package com.spring.german.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String technology;

    @ManyToMany(mappedBy = "technologies")
    private Set<Project> projects;

    public Technology(String technology) {
        this.technology = technology;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Technology{" +
                "id=" + id +
                ", technology='" + technology + '\'' +
                '}';
    }
}
