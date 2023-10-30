package ru.cryptopro.support.DssJavaClient.dto.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import ru.cryptopro.support.DssJavaClient.enums.OperationStatesEnum;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class OperationDto {
    private UUID id;
    private String type;
    private String parameters;
    private String description;
    private OperationStatesEnum state;
    private long createdAt;
    private long completeBefore;
    private long confirmBefore;
    private long confirmedAt;
    private long completedAt;
    private long updatedAt;
    private UUID userId;
    private String authenticationType;
    private String externalId;
    private List<OperationActionDto> actions;
    @JsonProperty("IsAsync")
    private boolean async;
    private String ssContext;
    private String callback;
    @JsonProperty("CallbackReceived")
    private boolean callbackReceived;

    public Date getCreatedAt() {
        return new Date(createdAt * 1000);
    }
    public Date getCompleteBefore() {
        return new Date(completeBefore * 1000);
    }
    public Date getConfirmBefore() {
        return new Date(confirmBefore * 1000);
    }
    public Date getConfirmedAt() {
        return new Date(confirmedAt * 1000);
    }
    public Date getCompletedAt() {
        return new Date(completedAt * 1000);
    }
    public Date getUpdatedAt() {
        return new Date(updatedAt * 1000);
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}