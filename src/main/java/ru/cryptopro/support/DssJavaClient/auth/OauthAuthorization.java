package ru.cryptopro.support.DssJavaClient.auth;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.cryptopro.support.DssJavaClient.auth.interfaces.Authorization;
import ru.cryptopro.support.DssJavaClient.config.DssApiCredConfig;
import ru.cryptopro.support.DssJavaClient.config.DssConfig;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.request.AuthRequest;
import ru.cryptopro.support.DssJavaClient.dto.response.AuthResponse;
import ru.cryptopro.support.DssJavaClient.enums.GrantTypeEnum;

@Component
@Log4j2
@Qualifier("OauthAuthorization")
public class OauthAuthorization implements Authorization {
    private final DssConfig dssConfig;
    private final DssApiCredConfig dssApiCredConfig;
    private final BasicAuthorization basicAuthorization;
    private final GrantTypeEnum grantType = GrantTypeEnum.PASSWORD;

    public OauthAuthorization(
            DssConfig dssConfig,
            DssApiCredConfig dssApiCredConfig,
            BasicAuthorization basicAuthorization
    ) {
        this.dssConfig = dssConfig;
        this.dssApiCredConfig = dssApiCredConfig;
        this.basicAuthorization = basicAuthorization;
    }

    @Override
    public AuthResponse login(UserCredentialDto userCredential) {
//      http://dss.cryptopro.ru/docs/articles/authentication/oauth/service-client.html
        if (dssApiCredConfig.isPublic() && Strings.isBlank(userCredential.getPassword()))
            log.warn("DssClient must be confidential if users password is not present. enabled identification only may help");

        AuthResponse response = basicAuthorization.login(
                AuthRequest.builder()
                        .grantType(grantType.getValue())
                        .username(userCredential.getUsername())
                        .password("")
                        .resource(dssConfig.getSignServerInstance())
                        .useDefaultScope()
                        .build().toMap()
        );

        log.info("Oauth authorization finished");
        return response;
    }
}
