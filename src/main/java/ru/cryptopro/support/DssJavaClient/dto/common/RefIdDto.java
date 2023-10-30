package ru.cryptopro.support.DssJavaClient.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class RefIdDto {
    @JsonProperty("RefId")
    UUID refId;

    public static List<RefIdDto> getInstance(List<UUID> refIds) {
        List<RefIdDto> list = new ArrayList<>();
        for (UUID walk : refIds)
            list.add(new RefIdDto(walk));
        return list;
    }
}
