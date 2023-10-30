package ru.cryptopro.support.DssJavaClient.config;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

@Configuration
@Log4j2
public class CertConfig {

    @Bean("certsFromCACerts")
    @SneakyThrows
    public Set<X509Certificate> getCertsFromCacerts() {
        Set<X509Certificate> result = new HashSet<>();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init((KeyStore) null);
        for (TrustManager trustManager : factory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager x509TrustManager) {
                result.addAll(Arrays.asList(x509TrustManager.getAcceptedIssuers()));
            }
        }
        log.info("Loaded {} certs from CaCerts", result.size());
        return result;
    }

    @Bean("certsFromFiles")
    @SneakyThrows
    public Set<X509Certificate> getCertsFromResource() {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        Set<X509Certificate> result = new HashSet<>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
        Resource[] resources = resolver.getResources("classpath:/certs/*");
        if (resources.length == 0) {
            log.warn("no certs found in classpath:/certs/");
            return result;
        }
        for (Resource resource : resources) {
            log.info("found {} in resource folder", resource.getFilename());
            result.add((X509Certificate) factory.generateCertificate(resource.getInputStream()));
        }

        return result;
    }
}
