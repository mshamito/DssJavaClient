package ru.cryptopro.support.DssJavaClient.dto.documents;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class AdditionalInfoDto {
    private Boolean smallFile;
    private String snippetTemplate;
    private String documentTemplate;
    private UUID linkedDocument;
}
