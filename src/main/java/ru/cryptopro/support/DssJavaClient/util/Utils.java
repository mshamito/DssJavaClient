package ru.cryptopro.support.DssJavaClient.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    private static final ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    public static Date getFutureDate(long seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, (int) seconds);
        return calendar.getTime();
    }
    @SneakyThrows
    public static String toJson(Object object) {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static String mapToStrings(Map<String,String> map) {
        String newline = System.lineSeparator();
        return newline + map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(newline));
    }
}
