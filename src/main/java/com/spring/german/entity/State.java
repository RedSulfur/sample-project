package com.spring.german.entity;

public enum State {

    /**
     * Each enum constant is declared with value for the state parameter.
     * These values are passed to the constructor when the constant is created.
     * Java requires that the constants be defined first, prior to any fields or methods.
     *
     * Also, when there are fields and methods,
     * the list of enum constants must end with a semicolon.
     */
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DELETED("Deleted"),
    LOCKED("Locked");

    private String state;

    State(final String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    /**
     * public final String name()
     * Returns the name of this enum constant, exactly as declared in its enum declaration.
     *
     * This method is designed primarily for use in specialized situations where
     * correctness depends on getting the exact name, which will not vary from release to release.
     */
    public String getName() {
        return this.name();
    }

    @Override
    public String toString() {
        return this.state;
    }
}
