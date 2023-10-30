package ru.cryptopro.support.DssJavaClient.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.cryptopro.support.DssJavaClient.entity.interfaces.AccessToken;
import ru.cryptopro.support.DssJavaClient.util.Consts;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.Date;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Schema(example = Consts.AUTH_RESPONSE_EXAMPLE)
public class AuthResponse {
    @Setter(AccessLevel.PRIVATE)
    private String accessToken;
    @Setter(AccessLevel.PRIVATE)
    private long expiresIn;
    private final String tokenType = "Bearer";
//    private String error;
//    private String errorDescription;

    public AuthResponse(AccessToken accessToken) {
        this.setAccessToken(accessToken.getToken());
        this.setExpiresIn((accessToken.getExpireAt().getTime() - new Date().getTime()) / 1000);
    }

    @Override
    public String toString() {
        return Utils.toJson(this);
    }
}
