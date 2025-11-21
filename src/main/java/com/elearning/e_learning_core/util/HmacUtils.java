package com.elearning.e_learning_core.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class HmacUtils {

    private HmacUtils() {

    }

    public static String generateHmacSHA256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexResult = new StringBuilder();
        for (byte b : rawHmac) {
            hexResult.append(String.format("%02x", b));
        }
        return hexResult.toString();
    }
}
