package com.elearning.e_learning_core.model;

public enum Role {
    ADMIN("ROLE_ADMIN"), INSTRUCTOR("ROLE_INSTRUCTOR"), STUDENT("ROLE_STUDENT");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}