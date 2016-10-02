package com.spring.german.entity;

public enum UserProfileType {

    /**
     * Each enum constant is declared with value for the role parameter.
     * This value is passed to the constructor when the constant is created.
     * Java requires that the constants be defined first, prior to any
     * fields or methods.
     */
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
