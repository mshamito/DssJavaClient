package ru.cryptopro.support.DssJavaClient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.cryptopro.support.DssJavaClient.config.CallbackConfig;
import ru.cryptopro.support.DssJavaClient.config.DssApiCredConfig;
import ru.cryptopro.support.DssJavaClient.config.DssConfig;
import ru.cryptopro.support.DssJavaClient.dto.common.RefIdDto;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.operation.OperationDto;
import ru.cryptopro.support.DssJavaClient.dto.request.RequestSecurityToken;
import ru.cryptopro.support.DssJavaClient.dto.request.SignatureOperation;
import ru.cryptopro.support.DssJavaClient.dto.response.RequestSecurityTokenResponse;
import ru.cryptopro.support.DssJavaClient.dto.response.SignResponse;
import ru.cryptopro.support.DssJavaClient.dto.sign.SignDocsListDto;
import ru.cryptopro.support.DssJavaClient.entity.ConfirmationEntity;
import ru.cryptopro.support.DssJavaClient.entity.repos.ConfirmationRepo;
import ru.cryptopro.support.DssJavaClient.enums.OperationStatesEnum;
import ru.cryptopro.support.DssJavaClient.enums.OperationStatusEnum;
import ru.cryptopro.support.DssJavaClient.exception.SignException;
import ru.cryptopro.support.DssJavaClient.util.Consts;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class SignService {
    private final DssConfig dssConfig;
    private final DssApiCredConfig apiCredConfig;
    private final AuthService authService;
    private final CallbackConfig callbackConfig;
    private final RestTemplate restTemplate;
    private final ConfirmationRepo confirmation;
    private final OperationService operationService;
    private final static String SIGN_V2_ENDPOINT = "/rest/api/v2/signature";
    private final static String CONFIRM_V2_ENDPOINT = "/v2.0/confirmation";


    public SignResponse sign(UserCredentialDto userCredential, SignDocsListDto signDocsList) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.login(userCredential).getAccessToken());

        boolean forceConfirm = signDocsList.getParams().isForceConfirmation();
        boolean isAsync = true;

        SignatureOperation signatureOperation = SignatureOperation.builder()
                .binaryData(RefIdDto.getInstance(signDocsList.getDocs()))
                .isAsync(isAsync)
                .forceConfirmation(forceConfirm)
                .callback(callbackConfig.getFullSSCallbackUri())
                .signature(signDocsList.getDocumentSignatureDto())
                .build();

        String url = dssConfig.getSignServerAddress() + SIGN_V2_ENDPOINT;
        HttpEntity<String> entity = new HttpEntity<>(signatureOperation.toString(), headers);
        log.info("Starting sign for user {}", userCredential.getUsername());
        log.info("POST on URL: {}", url);
        log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
        log.debug("Request Body: {}", signatureOperation);
        try {
            ResponseEntity<SignResponse> signResponse = restTemplate.postForEntity(
                    url,
                    entity,
                    SignResponse.class
            );
            SignResponse createdOperation = Optional.ofNullable(signResponse.getBody())
                    .orElseThrow(() -> new SignException("Operation creation failed. Response Body is null"));
            OperationStatusEnum createdOperationStatus = createdOperation.getOperation().getStatus();
            log.info("Status: {}", signResponse.getStatusCode());
            log.debug("Response: {}", signResponse.getBody());
            log.info("Sign operation status {}", createdOperationStatus);

            // for synchronous operation returning response
            if (createdOperationStatus == OperationStatusEnum.Completed)
                return signResponse.getBody();

            // saving to database
            confirmation.save(ConfirmationEntity.builder()
                    .guid(createdOperation.getOperation().getId())
                    .expireAt(Utils.getFutureDate(1200)) // 20 min
                    .username(userCredential.getUsername())
                    .status(createdOperationStatus.name())
                    .build());

            // operation is async, but no confirmation is needed
            OperationDto operation = operationService.getInfo(userCredential, createdOperation.getOperation().getId().toString());
            if (operation.getState() == OperationStatesEnum.Confirmed)
                return signResponse.getBody();

            log.info("Creating confirmation");
            // https://dss.cryptopro.ru/docs/articles/dsssdk/dsssdk_usage.html#createTrans
            String confirmUrl = dssConfig.getStsAddress() + CONFIRM_V2_ENDPOINT;

            RequestSecurityToken requestSecurityToken = RequestSecurityToken.builder()
                    .resource(Consts.RESOURCE_PREFIX + dssConfig.getSignServerInstance())
                    .clientId(apiCredConfig.getApiClientId())
                    .clientSecret(apiCredConfig.getApiClientSecret())
                    .operationId(createdOperation.getOperation().getId())
                    .callbackUri(callbackConfig.getFullIDPCallbackUri())
                    .build();

            HttpEntity<String> confirmationEntity = new HttpEntity<>(requestSecurityToken.toString(), headers);

            log.info("POST on URL: {}", confirmUrl);
            log.debug("Request Headers: {}", Utils.mapToStrings(headers.toSingleValueMap()));
            log.debug("Request Body: {}", requestSecurityToken);

            ResponseEntity<RequestSecurityTokenResponse> response = restTemplate.postForEntity(
                    confirmUrl,
                    confirmationEntity,
                    RequestSecurityTokenResponse.class
            );
            log.info("Status: {}", response.getStatusCode());
            log.debug("Response: {}", response.getBody());

            return signResponse.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException(
                    String.format("Sign failed for user %s: %s", userCredential.getUsername(), e.getMessage())
            );
        }
    }
}
