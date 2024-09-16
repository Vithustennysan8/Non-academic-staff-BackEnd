package com.Non_academicWebsite.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.Non_academicWebsite.Entity.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE
            )
    ),
    SUPER_ADMIN(
            Set.of(
                    SUPER_ADMIN_CREATE,
                    SUPER_ADMIN_READ,
                    SUPER_ADMIN_UPDATE,
                    SUPER_ADMIN_DELETE,
                    ADMIN_DELETE,
                    ADMIN_UPDATE,
                    ADMIN_READ,
                    ADMIN_CREATE
            )
    );

    private final Set<Permission> permissionSet;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissionSet()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
