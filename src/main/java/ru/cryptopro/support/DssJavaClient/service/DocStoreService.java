package ru.cryptopro.support.DssJavaClient.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.cryptopro.support.DssJavaClient.config.DssConfig;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.documents.DocsListDto;
import ru.cryptopro.support.DssJavaClient.dto.documents.DocumentInfoDto;
import ru.cryptopro.support.DssJavaClient.dto.documents.GetDocumentInfoResultDto;
import ru.cryptopro.support.DssJavaClient.dto.response.AuthResponse;
import ru.cryptopro.support.DssJavaClient.exception.DocumentException;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Log4j2
@Service
public class DocStoreService {
    private final DssConfig dssConfig;
    private final AuthService authService;
    private final RestTemplate restTemplate;
    final static String ENDPOINT_PREFIX = "/api/documents/";
    final static String ENDPOINT_DOWNLOAD_POSTFIX = "/content";
    final static String UPLOAD_ENDPOINT = ENDPOINT_PREFIX + "multipartpack";
    final static String GET_INFO_ENDPOINT = ENDPOINT_PREFIX + "info";
    final static String GET_ALL_ENDPOINT = ENDPOINT_PREFIX + "user/docinfo/all";

    public DocStoreService(
            DssConfig dssConfig,
            AuthService authService,
            RestTemplate restTemplate
    ) {
        this.dssConfig = dssConfig;
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    //    @SneakyThrows
    public DocsListDto upload(
            UserCredentialDto userCredential,
            List<MultipartFile> multipartFileList
    ) {
        // https://dss.cryptopro.ru/docs/articles/rest/documentstore/endpoints/documents.html#upload_pack_multipart
        AuthResponse authResponse = authService.login(userCredential);
        List<DocumentInfoDto> documentInfoList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            documentInfoList.add(new DocumentInfoDto(multipartFile));
        }
        log.debug("Document list: {}", documentInfoList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(authResponse.getAccessToken());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (DocumentInfoDto walk : documentInfoList) {
            body.add(walk.getFilename() + "_info", walk.toString());
            MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
            ContentDisposition contentDisposition = ContentDisposition
                    .builder("form-data")
                    .name(walk.getFilename() + "_content")
                    .filename(walk.getFilename())
                    .build();
            fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            HttpEntity<byte[]> fileEntity = new HttpEntity<>(walk.getRawData(), fileMap);
            body.add(walk.getFilename() + "_content", fileEntity);
        }

        String url = dssConfig.getDocStoreAddress() + UPLOAD_ENDPOINT;
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        log.info("Loading documents to DocumentStore for user {}", userCredential.getUsername());
        log.info("POST on URL: {}", url);
        log.debug("Request Header: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        log.debug("Request Body (only keys): {}", body.keySet());
        for (String walk : body.keySet()) {
            if (walk.endsWith("_info"))
                log.debug("{}: {}", walk, body.get(walk));
        }
        try {
            ResponseEntity<DocsListDto> response = restTemplate.postForEntity(
                    url,
                    entity,
                    DocsListDto.class
            );
            log.info("Status: {}", response.getStatusCode());
            log.debug("Response: {}", response.getBody());
            log.info("Documents loaded for user {}", userCredential.getUsername());
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DocumentException(String.format("Upload request failed for user %s: %s", userCredential.getUsername(), e.getMessage()));
        }
    }

    public ResponseEntity<byte[]> download(UserCredentialDto userCredential, UUID doc) {
        AuthResponse authResponse = authService.login(userCredential);
        Map<UUID, GetDocumentInfoResultDto> infoMap = getDocsInfo(userCredential, Collections.singletonList(doc));
        Map.Entry<UUID, GetDocumentInfoResultDto> entry = infoMap.entrySet().iterator().next();
        String filename = entry.getValue().getDocumentInfo().getName();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(authResponse.getAccessToken());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(headers);

        String url = dssConfig.getDocStoreAddress() + ENDPOINT_PREFIX + doc.toString() + ENDPOINT_DOWNLOAD_POSTFIX;

        log.info("GET on URL: {}", url);
        log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        log.info("Status: {}", response.getStatusCode());

        ContentDisposition contentDisposition = ContentDisposition.inline().filename(filename, StandardCharsets.UTF_8).build();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentDisposition(contentDisposition);

        log.info("File {} downloaded for user {}", filename, userCredential.getUsername());

        return ResponseEntity.ok().headers(responseHeaders).body(response.getBody());
    }

    public Map<UUID, String> delete(UserCredentialDto userCredential, List<UUID> docs) {
        AuthResponse authResponse = authService.login(userCredential);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(authResponse.getAccessToken());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(headers);

        String url = dssConfig.getDocStoreAddress() + ENDPOINT_PREFIX;
        log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        Map<UUID, String> result = new HashMap<>();

        for (UUID walk : docs) {
            try {
                log.info("Deleting document. Id: {}", walk);
                log.info("DELETE on URL: {}", url + walk);
                try {
                    restTemplate.exchange(
                            url + walk,
                            HttpMethod.DELETE,
                            entity,
                            new ParameterizedTypeReference<>() {}
                    );
                    result.put(walk, "deleted");
                } catch (RestClientException e) {
                    throw new DocumentException(String.format("Failed deleting document. Id: %s, Exception: %s", walk, e.getMessage()));
                }
            } catch (DocumentException e) {
                result.put(walk, e.getMessage());
            }
        }
        return result;
    }

    public Map<UUID, GetDocumentInfoResultDto> getDocsInfo(UserCredentialDto userCredential, List<UUID> docs) {
        AuthResponse authResponse = authService.login(userCredential);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authResponse.getAccessToken());

        String url = dssConfig.getDocStoreAddress() + GET_INFO_ENDPOINT;
        DocsListDto requestBody = new DocsListDto(docs);
        HttpEntity<DocsListDto> entity = new HttpEntity<>(requestBody, headers);
        log.info("Getting information about documents from DocumentStore");
        log.info("POST on URL: {}", url);
        log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        log.debug("Request Body: {}", requestBody);

        ResponseEntity<Map<UUID, GetDocumentInfoResultDto>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        log.info("Status: {}", response.getStatusCode());
        log.debug("Response: {}", Utils.toJson(response.getBody()));

        return response.getBody();
    }

    public List<UUID> getAllDocs(UserCredentialDto userCredential) {
        AuthResponse authResponse = authService.login(userCredential);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authResponse.getAccessToken());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        String url = dssConfig.getDocStoreAddress() + GET_ALL_ENDPOINT;
        log.info("GET on URL: {}", url);
        log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        ResponseEntity<List<DocumentInfoDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );
        List<UUID> result = new ArrayList<>();
        if (response.getBody() == null)
            throw new DocumentException("Response body is null");
        for (DocumentInfoDto walk : response.getBody()) {
            result.add(walk.getId());
        }

        log.info("Status: {}", response.getStatusCode());
        log.debug("Response: {}", Utils.toJson(response.getBody()));

        return result;
    }
}
