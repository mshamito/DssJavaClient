package ru.cryptopro.support.DssJavaClient.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.cryptopro.support.DssJavaClient.auth.interfaces.Authorization;
import ru.cryptopro.support.DssJavaClient.config.DssApiCredConfig;
import ru.cryptopro.support.DssJavaClient.config.DssConfig;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.request.AuthRequest;
import ru.cryptopro.support.DssJavaClient.dto.response.AuthResponse;
import ru.cryptopro.support.DssJavaClient.enums.GrantTypeEnum;
import ru.cryptopro.support.DssJavaClient.util.Utils;

@Component
@Log4j2
@Qualifier("BasicAuthorization")
@RequiredArgsConstructor
public class BasicAuthorization implements Authorization {
    private final DssConfig dssConfig;
    private final DssApiCredConfig dssApiCredConfig;
    private final RestTemplate restTemplate;
    private final GrantTypeEnum grantType = GrantTypeEnum.PASSWORD;
    private final static String ENDPOINT = "/oauth/token";


    @Override
    public AuthResponse login(UserCredentialDto userCredential) {
        return login(AuthRequest.builder()
                .grantType(grantType.getValue())
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .resource(dssConfig.getSignServerInstance())
                .useDefaultRedirectUri()
                .build().toMap());
    }

    public AuthResponse login(MultiValueMap<String, String> requestBody) {

//      http://dss.cryptopro.ru/docs/articles/authentication/oauth/resourceowner-flow.html
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        if (dssApiCredConfig.isPublic()) { // public api DssClient
            if (!requestBody.containsKey("client_id"))
                requestBody.add("client_id", dssApiCredConfig.getApiClientId());
            if (!requestBody.containsKey("password"))
                requestBody.add("password", "");
        } else { // confidential api DssClient
            headers.setBasicAuth(dssApiCredConfig.getApiClientId(), dssApiCredConfig.getApiClientSecret());
        }
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        String url = dssConfig.getStsAddress() + ENDPOINT;
        log.info("POST on URL: {}", url);
        log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        log.debug("Request Body: {}", Utils.mapToStrings(requestBody.toSingleValueMap()));
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                url,
                entity,
                AuthResponse.class
        );
        log.info("Status: {}", response.getStatusCode());
        log.debug("Response: {}", response.getBody());
        return response.getBody();
    }
}
