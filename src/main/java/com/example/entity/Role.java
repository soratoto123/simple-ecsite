package com.example.entity;

public enum Role {
    ROLE_USER("一般"),
    ROLE_ADMIN("管理者");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
