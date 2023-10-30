package ru.cryptopro.support.DssJavaClient.dto.sign;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ResultDto {
    List<OperationResultInfoDto> processedDocuments;
}
