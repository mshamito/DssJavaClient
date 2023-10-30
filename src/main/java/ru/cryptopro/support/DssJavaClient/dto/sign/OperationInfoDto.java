package ru.cryptopro.support.DssJavaClient.dto.sign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import ru.cryptopro.support.DssJavaClient.dto.sign.ResultDto;
import ru.cryptopro.support.DssJavaClient.enums.OperationStatusEnum;

import java.util.Date;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OperationInfoDto {
    private UUID id;
    private ResultDto result;
    private OperationStatusEnum status;
    private String error;
    private String errorDescription;
    private long expirationDate;
    private String userLogin;
    private String userRealm;
    private String userId;

    public Date getExpirationDate() {
        return new Date(expirationDate * 1000);
    }
}