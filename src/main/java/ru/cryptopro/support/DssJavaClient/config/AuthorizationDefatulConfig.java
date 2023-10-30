package ru.cryptopro.support.DssJavaClient.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.cryptopro.support.DssJavaClient.auth.interfaces.Authorization;

@Configuration
public class AuthorizationDefatulConfig {
    private final Authorization basic;
    private final Authorization oAuth;
    private final DssApiCredConfig config;

    public AuthorizationDefatulConfig(
            @Qualifier("BasicAuthorization")
            Authorization basic,
            @Qualifier("OauthAuthorization")
            Authorization oAuth,
            DssApiCredConfig config
    ) {
        this.basic = basic;
        this.oAuth = oAuth;
        this.config = config;
    }

    @Primary
    @Bean
    public Authorization getDefaultAuthorizationInstance() {
        return config.isPublic() ? basic : oAuth;
    }
}
