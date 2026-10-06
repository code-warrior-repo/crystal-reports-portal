package com.company.reports.audit;

import com.company.reports.security.CurrentUserService;
import java.time.Instant;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportAuditService {

    private static final Logger log = LoggerFactory.getLogger(ReportAuditService.class);

    private final NamedParameterJdbcTemplate jdbc;
    private final CurrentUserService currentUserService;

    public ReportAuditService(NamedParameterJdbcTemplate jdbc, CurrentUserService currentUserService) {
        this.jdbc = jdbc;
        this.currentUserService = currentUserService;
    }

    public void success(String reportCode, String action, String parametersJson) {
        insert(reportCode, action, parametersJson, "SUCCESS", null);
    }

    public void failure(String reportCode, String action, String parametersJson, Exception exception) {
        String message = exception.getMessage();
        insert(reportCode, action, parametersJson, "FAILURE", message);
    }

    private void insert(String reportCode, String action, String parametersJson, String status, String message) {
        String sql = """
            INSERT INTO report_audit
              (user_id, report_code, action, parameters_json, status, message, created_at)
            VALUES
              (:userId, :reportCode, :action, :parametersJson, :status, :message, :createdAt)
            """;
        try {
            jdbc.update(sql, Map.of(
                "userId", currentUserService.requiredUser().userId(),
                "reportCode", reportCode,
                "action", action,
                "parametersJson", parametersJson == null ? "" : parametersJson,
                "status", status,
                "message", message == null ? "" : message,
                "createdAt", java.sql.Timestamp.from(Instant.now())
            ));
        } catch (Exception exception) {
            log.warn("Unable to write report audit row for report {}", reportCode, exception);
        }
    }
}
