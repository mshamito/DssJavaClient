package ru.cryptopro.support.DssJavaClient.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.CryptoPro.ssl.Provider;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

@Configuration
public class SSLContextConfig {
    private final TrustManager[] trustManagers;
    private final KeyManager[] keyManagers;

    public SSLContextConfig(
            @Qualifier("tmfGost")
//            @Qualifier("tmfTrustAny")
            TrustManager[] trustManagers,
            @Qualifier("gostKeyManagers")
            KeyManager[] keyManagers
    ) {
        this.trustManagers = trustManagers;
        this.keyManagers = keyManagers;
    }

    @Bean("gostCtx")
    @SneakyThrows
    public SSLContext getTlsInstance() {
        SSLContext context = SSLContext.getInstance(Provider.ALGORITHM_12);
        context.init(null, trustManagers, null);
        return context;
    }

    @Bean("mutualGostCtx")
    @SneakyThrows
    public SSLContext getMTlsInstance() {
        SSLContext context = SSLContext.getInstance(Provider.ALGORITHM_12);
        context.init(keyManagers, trustManagers, null);
        return context;
    }
}
