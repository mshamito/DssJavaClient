package ru.cryptopro.support.DssJavaClient.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class RequestSecurityToken {
    private String callbackUri;
    private UUID operationId;
    private String resource;
    private String clientId;
    private String clientSecret;
    private Object challengeResponse;
    private String confirmationScope;
    private Map<String,String> confirmationParams;
    private List<String> confirmationDataRefs;

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
