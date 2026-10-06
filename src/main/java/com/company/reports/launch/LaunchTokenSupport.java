package com.company.reports.launch;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class LaunchTokenSupport {

    private LaunchTokenSupport() {
    }

    public static String createToken(String userId, String sharedSecret) {
        String userPart = encode(userId);
        String issuedAtPart = encode(Long.toString(Instant.now().getEpochSecond()));
        String noncePart = encode(UUID.randomUUID().toString());
        String payload = userPart + "." + issuedAtPart + "." + noncePart;
        return payload + "." + sign(payload, sharedSecret);
    }

    private static String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String sign(String value, String sharedSecret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(sharedSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to sign launch token", exception);
        }
    }
}
