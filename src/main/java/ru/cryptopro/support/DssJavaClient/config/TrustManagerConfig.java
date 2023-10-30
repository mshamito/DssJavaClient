package ru.cryptopro.support.DssJavaClient.config;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.ssl.Provider;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Configuration
@Log4j2
public class TrustManagerConfig {
    private final Set<X509Certificate> certs;

    public TrustManagerConfig(
            @Qualifier("certsFromCACerts")
            Set<X509Certificate> certs
    ) {
        this.certs = certs;
    }

    @Bean("tmfGost")
    @SneakyThrows
    public TrustManager[] getGostTrustManager(){
        KeyStore keyStore = KeyStore.getInstance(JCP.CERT_STORE_NAME);
        keyStore.load( null, null);
        for (X509Certificate cert : certs) {
            keyStore.setCertificateEntry(UUID.randomUUID().toString(), cert);
        }
        log.info("Trusted Store size is {} ", Collections.list(keyStore.aliases()).size());
        TrustManagerFactory factory = TrustManagerFactory.getInstance(Provider.TRUSTMANGER_ALG);
        factory.init(keyStore);
        return factory.getTrustManagers();
    }

    @Bean("tmfTrustAny")
    @SneakyThrows
    public TrustManager[] getTrustManagerTrustAny() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                            throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                            throws CertificateException {
                    }
                }
        };
    }
}