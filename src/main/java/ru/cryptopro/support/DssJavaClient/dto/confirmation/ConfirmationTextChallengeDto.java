package ru.cryptopro.support.DssJavaClient.dto.confirmation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ConfirmationTextChallengeDto {
    private String label;
    private int expiresIn;
    private long createdAt;
    private boolean expiresInSpecified;
    @JsonProperty("IsHidden")
    private boolean isHidden;
    @JsonProperty("AuthnMethod")
    private String authMethod;
    @JsonProperty("RefID")
    private UUID refId;
    private String title;

    public Date getCreatedAt() {
        return new Date(createdAt * 1000);
    }
}
