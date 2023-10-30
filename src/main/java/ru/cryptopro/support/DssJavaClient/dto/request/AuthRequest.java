package ru.cryptopro.support.DssJavaClient.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.SneakyThrows;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.cryptopro.support.DssJavaClient.util.Consts;

import java.util.Map;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthRequest {
    String grantType;
    String username;
    String password;
    String resource;
    String redirectUri;
    String responseType;
    String scope;
    String clientId;
    String code;
    String actorToken;
    String actorTokenType;
    String subjectTokenType;
    String subjectToken;

    @SneakyThrows
    public MultiValueMap<String, String> toMap() {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> maps = mapper.convertValue(this, new TypeReference<>() {});
        parameters.setAll(maps);
        return parameters;
    }

    public static class AuthRequestBuilder {
        public AuthRequestBuilder useDefaultRedirectUri() {
            this.redirectUri = Consts.OAUTH_REDIRECT;
            return this;
        }
        public AuthRequestBuilder useDefaultScope() {
            this.scope = Consts.SCOPE;
            return this;
        }

        public AuthRequestBuilder resource(String resource) {
            this.resource = Consts.RESOURCE_PREFIX + resource;
            return this;
        }
    }
}
