package com.spring.german.entity;

public enum UserProfileType {

    USER("User"),
    DBA("Dba"),
    ADMIN("Admin");

    private String userProfileType;

    UserProfileType(String userProfileType) {
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType() {
        return this.userProfileType;
    }
}
