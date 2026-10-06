package com.company.reports.launch;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsedLaunchNonceRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public UsedLaunchNonceRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean markUsed(String nonce, String userId) {
        String sql = """
            INSERT INTO used_launch_nonce (nonce, user_id, used_at)
            VALUES (:nonce, :userId, :usedAt)
            """;
        try {
            jdbc.update(sql, Map.of(
                "nonce", nonce,
                "userId", userId,
                "usedAt", Timestamp.from(Instant.now())
            ));
            return true;
        } catch (DuplicateKeyException duplicate) {
            return false;
        }
    }
}
