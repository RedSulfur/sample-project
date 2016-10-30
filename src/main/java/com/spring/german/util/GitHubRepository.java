package com.spring.german.util;

public class GitHubRepository {

    private String repoName;
    private String ownerName;

    public GitHubRepository(String repoName, String ownerName) {
        this.repoName = repoName;
        this.ownerName = ownerName;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
