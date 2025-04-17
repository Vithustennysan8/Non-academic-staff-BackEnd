package com.Non_academicWebsite.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    MANAGER_READ("manager:read"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_CREATE("manager:create"),
    MANAGER_DELETE("manager:delete"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    SUPER_ADMIN_CREATE("super_admin:create"),
    SUPER_ADMIN_READ("super_admin:read"),
    SUPER_ADMIN_UPDATE("super_admin:update"),
    SUPER_ADMIN_DELETE("super_admin:delete");

    private final String permission;
}
