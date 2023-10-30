package ru.cryptopro.support.DssJavaClient.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import ru.cryptopro.support.DssJavaClient.dto.confirmation.ConfirmationChallengeDto;
import ru.cryptopro.support.DssJavaClient.util.Utils;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class RequestSecurityTokenResponse {
    private ConfirmationChallengeDto challenge;
    private String accessToken;
    private int expiresIn;
    private boolean isFinal;
    private boolean isError;
    private String error;
    private String errorDescription;

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
