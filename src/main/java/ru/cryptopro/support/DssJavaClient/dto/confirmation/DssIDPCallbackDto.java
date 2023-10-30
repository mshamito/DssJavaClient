package ru.cryptopro.support.DssJavaClient.dto.confirmation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DssIDPCallbackDto {
    private String result;
    private UUID transactionId;
    private String error;
    private String errorDescription;

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
