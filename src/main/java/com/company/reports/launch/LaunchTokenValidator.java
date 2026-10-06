package com.company.reports.launch;

import com.company.reports.config.ReportsProperties;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

@Service
public class LaunchTokenValidator {

    private final ReportsProperties properties;
    private final UsedLaunchNonceRepository nonceRepository;

    public LaunchTokenValidator(ReportsProperties properties, UsedLaunchNonceRepository nonceRepository) {
        this.properties = properties;
        this.nonceRepository = nonceRepository;
    }

    public LaunchToken validate(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Missing launch token");
        }

        String[] parts = token.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid launch token format");
        }

        String userId = decode(parts[0]);
        Instant issuedAt = Instant.ofEpochSecond(Long.parseLong(decode(parts[1])));
        String nonce = decode(parts[2]);
        String expectedSignature = sign(parts[0] + "." + parts[1] + "." + parts[2]);

        if (!constantTimeEquals(expectedSignature, parts[3])) {
            throw new IllegalArgumentException("Invalid launch token signature");
        }
        if (issuedAt.plus(properties.getLaunch().getMaxTokenAge()).isBefore(Instant.now())) {
            throw new IllegalArgumentException("Launch token expired");
        }
        if (!nonceRepository.markUsed(nonce, userId)) {
            throw new IllegalArgumentException("Launch token already used");
        }

        return new LaunchToken(userId, issuedAt, nonce);
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                properties.getLaunch().getSharedSecret().getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
            ));
            return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to sign launch token", exception);
        }
    }

    private String decode(String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }

    private boolean constantTimeEquals(String left, String right) {
        return java.security.MessageDigest.isEqual(
            left.getBytes(StandardCharsets.UTF_8),
            right.getBytes(StandardCharsets.UTF_8)
        );
    }
}
