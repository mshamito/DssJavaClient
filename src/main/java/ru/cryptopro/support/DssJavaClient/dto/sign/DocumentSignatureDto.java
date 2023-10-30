package ru.cryptopro.support.DssJavaClient.dto.sign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import ru.cryptopro.support.DssJavaClient.enums.SignatureTypeEnum;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Builder
@Getter
public class DocumentSignatureDto {
    private SignatureTypeEnum type;
    private SignatureParamsDto parameters;
    @Builder.Default
    private String certificateId = "0";
    private String pinCode;
    private String processingTemplateId;
}
