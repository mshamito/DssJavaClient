package ru.cryptopro.support.DssJavaClient.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateDefaultConfig {
    private final DssConfig config;
    private final RestTemplate rsa;
    private final RestTemplate gost;

    public RestTemplateDefaultConfig(
            DssConfig config,
            @Qualifier("defaultRsa")
            RestTemplate rsa,
            @Qualifier("gostTls")
            RestTemplate gost
    ) {
        this.config = config;
        this.rsa = rsa;
        this.gost = gost;
    }

    @Primary
    @Bean
    public RestTemplate getDefaultRestTemplateInstance() {
        return config.isUseGostForSingleTls() ? gost : rsa;
    }
}
