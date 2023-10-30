package ru.cryptopro.support.DssJavaClient.dto.documents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DocsListDto {
    @JsonProperty("DocumentIds")
    List<UUID> documentIds;

    public DocsListDto(List<UUID> documentIds) {
        this.documentIds = documentIds;
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
