package com.spring.german.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty
    @Column(name = "sso_id")
    private String ssoId;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "state")
    private String state = State.INACTIVE.getState();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "app_user_user_profile",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_profile_id") })
    private Set<UserProfile> userProfiles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects;

    public User() {
    }

    public User(String ssoId, String password,
                String email,
                String state,
                Set<UserProfile> userProfiles) {
        this.ssoId = ssoId;
        this.password = password;
        this.email = email;
        this.state = state;
        this.userProfiles = userProfiles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int hashCode() {
        final long prime = 31;
        int result = 1;
        result = (int) (prime * result + id);
        result = (int) (prime * result + ((ssoId == null) ? 0 : ssoId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        if (ssoId == null) {
            if (other.ssoId != null)
                return false;
        } else if (!ssoId.equals(other.ssoId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", ssoId=" + ssoId + ", password=" + password
                + ", email=" + email + ", state=" + state + ", userProfiles=" + userProfiles +"]";
    }

}