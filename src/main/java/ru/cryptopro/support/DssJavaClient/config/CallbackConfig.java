package ru.cryptopro.support.DssJavaClient.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CallbackConfig {
    @Value("${app.config.callback_host}")
    private String callbackHost;
    @Value("${app.config.ss_callback_endpoint}")
    private String SSCallback;
    @Value("${app.config.idp_callback_endpoint}")
    private String IDPCallback;

    public String getFullSSCallbackUri() { return callbackHost + SSCallback;}
    public String getFullIDPCallbackUri() { return callbackHost + IDPCallback;}
}
