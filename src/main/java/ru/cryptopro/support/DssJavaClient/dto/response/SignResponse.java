package ru.cryptopro.support.DssJavaClient.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import ru.cryptopro.support.DssJavaClient.dto.sign.OperationInfoDto;
import ru.cryptopro.support.DssJavaClient.util.Utils;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SignResponse {
    OperationInfoDto operation;

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
