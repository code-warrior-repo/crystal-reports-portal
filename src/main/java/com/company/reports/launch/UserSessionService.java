package com.company.reports.launch;

import com.company.reports.config.SecurityConfig;
import com.company.reports.security.CurrentUser;
import com.company.reports.security.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {

    private final RoleService roleService;

    public UserSessionService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void establishReportsSession(String userId, HttpServletRequest request) {
        Set<String> roles = roleService.findRolesForUser(userId);
        CurrentUser user = new CurrentUser(userId, userId, roles);
        SecurityConfig.establishSessionAuthentication(user, request);
    }
}
