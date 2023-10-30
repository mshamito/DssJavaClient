package ru.cryptopro.support.DssJavaClient.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class DssApiCredConfig {
    @Value("${dss.api_client.api_client_id}")
    private String apiClientId;
    @Value("${dss.api_client.api_client_secret:#{null}}")
    private String apiClientSecret;

    public boolean isPublic() {
        return apiClientSecret == null || apiClientSecret.trim().isEmpty();
    }
}
