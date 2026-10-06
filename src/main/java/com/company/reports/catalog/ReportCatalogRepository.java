package com.company.reports.catalog;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportCatalogRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public ReportCatalogRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ReportMenuItem> findMenuItemsForRoles(Collection<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }

        String sql = """
            SELECT
                g.code AS group_code,
                g.name AS group_name,
                r.code AS report_code,
                r.name AS report_name
            FROM report_group g
            JOIN report_definition r ON r.group_id = g.id
            JOIN report_role rr ON rr.report_id = r.id
            WHERE g.active = 'Y'
              AND r.active = 'Y'
              AND rr.role_code IN (:roles)
            ORDER BY g.sort_order, r.sort_order
            """;

        return jdbc.query(sql, Map.of("roles", roles), (rs, rowNum) ->
            new ReportMenuItem(
                rs.getString("group_code"),
                rs.getString("group_name"),
                rs.getString("report_code"),
                rs.getString("report_name")
            )
        );
    }

    public Optional<ReportDefinition> findDefinition(String reportCode) {
        String sql = """
            SELECT id, code, name, rpt_file, description
            FROM report_definition
            WHERE code = :reportCode
              AND active = 'Y'
            """;

        List<ReportDefinition> reports = jdbc.query(sql, Map.of("reportCode", reportCode), (rs, rowNum) ->
            new ReportDefinition(
                rs.getLong("id"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getString("rpt_file"),
                rs.getString("description")
            )
        );
        return reports.stream().findFirst();
    }

    public List<ReportParameter> findParameters(String reportCode) {
        String sql = """
            SELECT p.name, p.label, p.type, p.required, p.default_value
            FROM report_parameter p
            JOIN report_definition r ON r.id = p.report_id
            WHERE r.code = :reportCode
              AND r.active = 'Y'
            ORDER BY p.sort_order
            """;

        return jdbc.query(sql, Map.of("reportCode", reportCode), (rs, rowNum) ->
            new ReportParameter(
                rs.getString("name"),
                rs.getString("label"),
                rs.getString("type"),
                "Y".equalsIgnoreCase(rs.getString("required")),
                rs.getString("default_value")
            )
        );
    }

    public boolean userCanAccessReport(String reportCode, Collection<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }

        String sql = """
            SELECT COUNT(1)
            FROM report_definition r
            JOIN report_role rr ON rr.report_id = r.id
            WHERE r.code = :reportCode
              AND r.active = 'Y'
              AND rr.role_code IN (:roles)
            """;

        Integer count = jdbc.queryForObject(
            sql,
            new MapSqlParameterSource()
                .addValue("reportCode", reportCode)
                .addValue("roles", roles),
            Integer.class
        );
        return count != null && count > 0;
    }

    public Map<String, ReportGroup> groupMenuItems(List<ReportMenuItem> items) {
        Map<String, ReportGroup> groups = new LinkedHashMap<>();
        Map<String, List<ReportMenuItem>> grouped = items.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                ReportMenuItem::groupCode,
                LinkedHashMap::new,
                java.util.stream.Collectors.toList()
            ));

        grouped.forEach((code, groupItems) -> groups.put(
            code,
            new ReportGroup(code, groupItems.getFirst().groupName(), groupItems)
        ));
        return groups;
    }
}
