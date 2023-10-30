package ru.cryptopro.support.DssJavaClient.exception.handlers.helper;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessageHelper {
    public static Map<String, Object> getMessageBody(Exception message) {
        return new HashMap<>() {{
            put("error", true);
            put("errorMsg", message.getMessage());
        }};
    }
}
