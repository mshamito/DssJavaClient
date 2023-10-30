package ru.cryptopro.support.DssJavaClient.dto.confirmation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class ConfirmationChallengeDto {
    private ConfirmationTitleDto title;
    private List<ConfirmationTextChallengeDto> textChallenge;
}
