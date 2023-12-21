package ru.cryptopro.support.DssJavaClient.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtGenUtil {
    private static final long timeout = 3600L;

    public static String getToken(String username) {
        return  getToken(username, false);
    }

    @SneakyThrows
    public static String getToken(String username, boolean useNameIdentifier) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> header = new HashMap<>();
        header.put("alg", "none");
        header.put("typ", "JWT");

        Map<String, Object> payload = new HashMap<>();
        payload.put(useNameIdentifier? "nameidentifier" : "unique_name", username);
        long unixTime = System.currentTimeMillis() / 1000L;
//        long unixTime = new Date().getTime();
        long expireTime = unixTime + timeout;
        payload.put("nbf", unixTime);
        payload.put("exp", expireTime);
        payload.put("iat", unixTime);

        // Base64UrlEncode(Utf8GetBytes(header)) + “.” + Base64UrlEncode(Utf8GetBytes(payload)) + “.”
        return (Base64.getUrlEncoder().withoutPadding().encodeToString(
                mapper.writeValueAsString(header).getBytes(StandardCharsets.UTF_8))
                + "." +
                Base64.getUrlEncoder().withoutPadding().encodeToString(
                        mapper.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8)
                )
                + ".");
    }
}
