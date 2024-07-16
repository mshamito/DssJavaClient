package ru.cryptopro.support.DssJavaClient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.cryptopro.support.DssJavaClient.config.DssConfig;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.operation.OperationDto;
import ru.cryptopro.support.DssJavaClient.dto.response.AuthResponse;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class OperationService {
    private final DssConfig dssConfig;

    private final AuthService authService;
    private final RestTemplate restTemplate;

    final static String ENDPOINT = "/operations";


    public List<OperationDto> getAllActive(UserCredentialDto userCredential) {
        AuthResponse authResponse = authService.login(userCredential);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getAccessToken());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(headers);

        String url = dssConfig.getStsAddress() + ENDPOINT;

        log.info("GET on URL: {}", url);
        log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        ResponseEntity<List<OperationDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );
        if (response.getBody() == null)
            throw new RuntimeException("Response body is null");

        return response.getBody();
    }

    public OperationDto getInfo(UserCredentialDto userCredential, String id) {
        AuthResponse authResponse = authService.login(userCredential);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authResponse.getAccessToken());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(headers);

        String url = dssConfig.getStsAddress() + ENDPOINT + "/" + id;

        log.info("GET on URL: {}", url);
        log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        ResponseEntity<OperationDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );
        if (response.getBody() == null)
            throw new RuntimeException("Response body is null");
        log.info("Status: {}", response.getStatusCode());
        log.debug("Response: {}", response.getBody());

        return response.getBody();
    }
}
