package ru.cryptopro.support.DssJavaClient.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class DssConfig {
    @Value("${dss.config.base_url}")
    private String baseUrl;
    @Value("${dss.config.sts_instance:STS}")
    private String stsInstance;
    @Value("${dss.config.sign_server_instance:SignServer}")
    private String signServerInstance;
    @Value("${dss.config.doc_store_instance:DocumentStore}")
    private String docStoreInstance;
    @Value("${dss.config.use_gost_for_single_tls:false}")
    private boolean useGostForSingleTls;
    @Value("${dss.config.rsa_port:443}")
    private int rsaPort;
    @Value("${dss.config.gost_port:4430}")
    private int gostPort;

    private String getUrlWithPort(int port) {
        if (port == 443)
            return baseUrl;
        return baseUrl + ":" + port;
    }

    public String getStsAddress() {
        int port = useGostForSingleTls ? gostPort : rsaPort;
        return getUrlWithPort(port) + "/" + getStsInstance();
    }

    public String getStsAddressForCertAuth() {
        return getUrlWithPort(gostPort) + "/" + getStsInstance();
    }

    public String getSignServerAddress() {
        int port = useGostForSingleTls ? gostPort : rsaPort;
        return getUrlWithPort(port) + "/" + getSignServerInstance();
    }

    public String getDocStoreAddress() {
        int port = useGostForSingleTls ? gostPort : rsaPort;
        return getUrlWithPort(port) + "/" + getDocStoreInstance();
    }
}
