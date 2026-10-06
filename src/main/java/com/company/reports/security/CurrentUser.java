package com.company.reports.security;

import java.io.Serializable;
import java.util.Set;

public record CurrentUser(String userId, String displayName, Set<String> roles) implements Serializable {

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean hasAnyRole(Iterable<String> requiredRoles) {
        for (String requiredRole : requiredRoles) {
            if (roles.contains(requiredRole)) {
                return true;
            }
        }
        return false;
    }
}
