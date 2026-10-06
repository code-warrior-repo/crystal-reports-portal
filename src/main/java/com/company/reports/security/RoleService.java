package com.company.reports.security;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final NamedParameterJdbcTemplate jdbc;

    public RoleService(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Set<String> findRolesForUser(String userId) {
        String sql = """
            SELECT role_code
            FROM app_user_role
            WHERE user_id = :userId
            ORDER BY role_code
            """;
        return new LinkedHashSet<>(jdbc.queryForList(sql, Map.of("userId", userId), String.class));
    }
}
