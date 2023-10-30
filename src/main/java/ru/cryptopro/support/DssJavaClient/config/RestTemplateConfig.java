package ru.cryptopro.support.DssJavaClient.config;

import lombok.SneakyThrows;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;

@Configuration
public class RestTemplateConfig extends RestTemplate {
    @Bean("defaultRsa")
    public RestTemplate getDefaultInstance() {
        return new RestTemplate();
    }

    @Bean("gostTls")
    public RestTemplate getGostInstance(
            @Qualifier("gostCtx")
            SSLContext sslContext
    ) {
        return getRestTemplate(sslContext,false);
    }

    @Bean("mutualGostTls")
    public RestTemplate getMutualGostInstance(
            @Qualifier("mutualGostCtx")
            SSLContext sslContext
    ) {
        return getRestTemplate(sslContext,true);
    }

    @Bean("insecureRsaTls")
    @SneakyThrows
    public RestTemplate getTrustAnyInstance() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        return getRestTemplate(sslContext, false);
    }

    private RestTemplate getRestTemplate(SSLContext sslContext, boolean disableRedirect) {
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(socketFactory).build();
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionManager(connectionManager);
        if (disableRedirect)
            builder.disableRedirectHandling();
        CloseableHttpClient httpClient = builder.build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }
}
