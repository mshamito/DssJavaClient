package ru.cryptopro.support.DssJavaClient.config;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.ssl.JavaTLSCertPathManagerParameters;
import ru.CryptoPro.ssl.Provider;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.security.KeyStore;
import java.security.cert.*;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Configuration
@Log4j2
public class KeyManagerConfig {
    private final KeyStore keyStore;
    private final KeyStoreConfig keyStoreConfig;
    private final Set<X509Certificate> certsFromFiles;
    private final char[] password;

    public KeyManagerConfig(
            @Qualifier("gostKeyStore")
            KeyStore keyStore,
            KeyStoreConfig keyStoreConfig,
            @Qualifier("certsFromFiles")
            Set<X509Certificate> certsFromFiles
    ) {
        this.keyStore = keyStore;
        this.keyStoreConfig = keyStoreConfig;
        this.certsFromFiles = certsFromFiles;
        this.password = keyStoreConfig.getPassword().toCharArray();
    }

    @Bean("gostKeyManagers")
    @SneakyThrows
    public KeyManager[] getKeyManagers() {

        KeyManagerFactory factory = KeyManagerFactory.getInstance(Provider.KEYMANGER_ALG);
        if (keyStoreConfig.isChainNeeded()) {
            log.warn("Need certificate chain for your alias. using local certificates");
            KeyStore trustStore = KeyStore.getInstance(JCP.CERT_STORE_NAME);
            trustStore.load(null,null);
            for (X509Certificate certificate : certsFromFiles)
                trustStore.setCertificateEntry(UUID.randomUUID().toString(), certificate);
            PKIXBuilderParameters parameters = new PKIXBuilderParameters(trustStore, new X509CertSelector());
            parameters.setRevocationEnabled(true);
            parameters.setCertStores(Collections.singletonList(CertStore.getInstance("Collection", new CollectionCertStoreParameters(certsFromFiles))));
            JavaTLSCertPathManagerParameters managerParameters = new JavaTLSCertPathManagerParameters(keyStore, password);
            managerParameters.setParameters(parameters);
            factory.init(managerParameters);
            return factory.getKeyManagers();
        }
        factory.init(keyStore, password);
        return factory.getKeyManagers();
    }
}
