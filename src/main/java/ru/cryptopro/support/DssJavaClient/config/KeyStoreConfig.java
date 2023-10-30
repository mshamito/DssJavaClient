package ru.cryptopro.support.DssJavaClient.config;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.CryptoPro.JCP.KeyStore.StoreInputStream;

import java.security.KeyStore;
import java.security.cert.Certificate;

@Configuration
@Getter
@Log4j2
public class KeyStoreConfig {
    @Value("${app.keystore.name}")
    private String keyStoreName;
    @Value("${app.keystore.alias}")
    private String alias;
    @Value("${app.keystore.password:}")
    private String password;
    private boolean isChainNeeded = false;

    @Bean("gostKeyStore")
    @SneakyThrows
    public KeyStore getGostKeyStore() {
        KeyStore keyStore = KeyStore.getInstance(keyStoreName);
        keyStore.load(new StoreInputStream(alias), null);
        Certificate[] chain = keyStore.getCertificateChain(alias);

        log.debug("chain length: {} ", chain.length);

        if (chain.length < 2)
            isChainNeeded = true;

        log.info("KeyStore loaded. KeyStore: {} , alias: {}", keyStoreName, alias);
        return keyStore;
    }
}
