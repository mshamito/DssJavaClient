package ru.cryptopro.support.DssJavaClient.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import ru.cryptopro.support.DssJavaClient.dto.common.RefIdDto;
import ru.cryptopro.support.DssJavaClient.dto.sign.DocumentSignatureDto;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class SignatureOperation {
    private List<RefIdDto> binaryData;
    private DocumentSignatureDto signature;
    @Builder.Default
    @JsonProperty("isAsync")
    private boolean isAsync = true;
    private String callback;
    private boolean forceConfirmation;

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
