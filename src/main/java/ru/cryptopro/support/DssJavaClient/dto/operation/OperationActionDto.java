package ru.cryptopro.support.DssJavaClient.dto.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import ru.cryptopro.support.DssJavaClient.enums.OperationActionStatesEnum;
import ru.cryptopro.support.DssJavaClient.enums.OperationActionStatusesEnum;

import java.util.UUID;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OperationActionDto {
    private UUID id;
    private UUID documentId;
    private UUID originalDocumentId;
    private OperationActionStatusesEnum status;
    private OperationActionStatesEnum state;
    private UUID resultValue;
    private String error;
    private String errorDescription;
}
