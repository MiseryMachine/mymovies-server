package com.rjs.mymovies.server.model.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {
    ROLE_USER("User"),
    ROLE_REST_USER("REST User"),
    ROLE_ADMIN("Admin");

    public final String text;

    Role(String text) {
        this.text = text;
    }

    public static Role findByText(String roleText) {
        for (Role role : Role.values()) {
            if (role.text.equals(roleText)) {
                return role;
            }
        }

        return null;
    }

    public static List<String> getRoleText() {
        return Arrays.stream(Role.values())
            .map(r -> r.text)
            .collect(Collectors.toList());
    }
}
