package ru.cryptopro.support.DssJavaClient.dto.sign;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.cryptopro.support.DssJavaClient.util.Consts;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Schema(example = Consts.SIGN_REQUEST_EXAMPLE)
public class SignDocsListDto {
    private List<UUID> docs;
    @JsonProperty("Signature")
    private DocumentSignatureDto documentSignatureDto;
    private OperationParamDto params;

    public OperationParamDto getParams() {
        if (params == null) { // default values
            OperationParamDto param = new OperationParamDto();
            param.setForceConfirmation(false);
            return param;
        }
        return params;
    }
}
