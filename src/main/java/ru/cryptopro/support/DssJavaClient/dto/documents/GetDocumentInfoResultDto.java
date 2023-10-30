package ru.cryptopro.support.DssJavaClient.dto.documents;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import ru.cryptopro.support.DssJavaClient.dto.documents.DocumentInfoDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class GetDocumentInfoResultDto {
    private boolean found;
    private DocumentInfoDto documentInfo;
}
