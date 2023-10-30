package ru.cryptopro.support.DssJavaClient.dto.sign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import ru.cryptopro.support.DssJavaClient.enums.OperationResultStatusesEnum;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Data
public class OperationResultInfoDto {
    private UUID refId;
    private String contentInfo;
    private UUID originalRefId;
    private String originalContentInfo;
    private OperationResultStatusesEnum status;
    private String error;
    private String errorDescription;
}
