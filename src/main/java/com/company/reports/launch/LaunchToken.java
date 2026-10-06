package com.company.reports.launch;

import java.time.Instant;

public record LaunchToken(String userId, Instant issuedAt, String nonce) {
}
