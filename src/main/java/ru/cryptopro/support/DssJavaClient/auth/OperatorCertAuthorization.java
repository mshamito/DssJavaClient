package ru.cryptopro.support.DssJavaClient.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cryptopro.support.DssJavaClient.auth.interfaces.Authorization;
import ru.cryptopro.support.DssJavaClient.config.DssApiCredConfig;
import ru.cryptopro.support.DssJavaClient.config.DssConfig;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.request.AuthRequest;
import ru.cryptopro.support.DssJavaClient.dto.response.AuthResponse;
import ru.cryptopro.support.DssJavaClient.entity.OperatorAccessTokenEntity;
import ru.cryptopro.support.DssJavaClient.entity.interfaces.AccessToken;
import ru.cryptopro.support.DssJavaClient.enums.ActorTokenTypeEnum;
import ru.cryptopro.support.DssJavaClient.enums.GrantTypeEnum;
import ru.cryptopro.support.DssJavaClient.enums.ResponseTypeEnum;
import ru.cryptopro.support.DssJavaClient.enums.SubjectTokenTypeEnum;
import ru.cryptopro.support.DssJavaClient.exception.AuthorizationException;
import ru.cryptopro.support.DssJavaClient.entity.repos.OperatorAccessTokenRepo;
import ru.cryptopro.support.DssJavaClient.util.JwtGenUtil;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Log4j2
@Qualifier("OperatorCertAuthorization")
public class OperatorCertAuthorization implements Authorization {
    private final DssConfig dssConfig;
    private final DssApiCredConfig dssApiCredConfig;
    private final BasicAuthorization basicAuthorization;
    private final OperatorAccessTokenRepo operatorAccessTokenRepo;

    private final RestTemplate restTemplate;

    @Value("${dss.api_client.use_cache:true}")
    private boolean useCache;

    final static String ENDPOINT = "/oauth/authorize/certificate";
    static final long OPERATOR_ID_FOR_DB = 1; // operator always one

    public OperatorCertAuthorization(
            DssConfig dssConfig,
            DssApiCredConfig dssApiCredConfig,
            BasicAuthorization basicAuthorization,
            OperatorAccessTokenRepo operatorAccessTokenRepo,
            @Qualifier("mutualGostTls")
            RestTemplate restTemplate
    ) {
        this.dssConfig = dssConfig;
        this.dssApiCredConfig = dssApiCredConfig;
        this.basicAuthorization = basicAuthorization;
        this.operatorAccessTokenRepo = operatorAccessTokenRepo;
        this.restTemplate = restTemplate;
    }

    @Override
    //      http://dss.cryptopro.ru/docs/articles/authentication/oauth/actas-token.html
    public AuthResponse login(UserCredentialDto userCredential) {

        AuthResponse operatorResponse = getOperatorToken();

        if (userCredential.retrieveOperatorTokenOnly()) {
            // getting operator token only, for UMS scenarios
            return operatorResponse;
        }

        // getting delegated token
        String jwtToken = JwtGenUtil.getToken(userCredential.getUsername());
        log.debug("jwt token: {}", jwtToken);

        log.info("Getting the Delegating Access Token");

        AuthResponse result = basicAuthorization.login(
                AuthRequest.builder()
                        .grantType(GrantTypeEnum.TOKEN_EXCHANGE.getValue())
                        .resource(dssConfig.getSignServerInstance())
                        .actorToken(operatorResponse.getAccessToken())
                        .actorTokenType(ActorTokenTypeEnum.JWT.getValue())
                        .subjectTokenType(SubjectTokenTypeEnum.JWT.getValue())
                        .subjectToken(jwtToken)
                        .build().toMap()
        );

        log.info("Delegating access token received");

        return result;
    }

    private AuthResponse getOperatorToken() {

        if (useCache) {
            Optional<? extends AccessToken> optionalOperatorAccessToken = operatorAccessTokenRepo.findFirstByOperator(OPERATOR_ID_FOR_DB);
            if (optionalOperatorAccessToken.isPresent()) {
                AccessToken cachedToken = optionalOperatorAccessToken.get();
                AuthResponse cachedResponse = new AuthResponse(cachedToken);
                log.info("Cached operators token returned, expire at: {}", cachedToken.getExpireAt());
                return cachedResponse;
            }
        }
        log.info("Initiating operator authentication");
        MultiValueMap<String, String> queryParam = AuthRequest.builder()
                .resource(dssConfig.getSignServerInstance())
                .clientId(dssApiCredConfig.getApiClientId())
                .responseType(ResponseTypeEnum.CODE.getValue())
                .useDefaultRedirectUri()
                .useDefaultScope()
                .build().toMap();

        String url = dssConfig.getStsAddressForCertAuth() + ENDPOINT;
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParam).build();

        log.info("Getting the authorization code");
        log.info("GET on URL: {}", url);
        log.debug("Request Query: {}", Utils.mapToStrings(queryParam.toSingleValueMap()));
        ResponseEntity<AuthResponse> certResponse = restTemplate.getForEntity(
                uriComponents.toUriString(),
                AuthResponse.class
        );

        List<String> locationList = certResponse.getHeaders().get("Location");
        if (locationList == null || locationList.isEmpty())
            throw new AuthorizationException("Header Location is not present");
        String location = locationList.get(0);


        log.info("Status: {}", certResponse.getStatusCode());
        log.debug("Location: {}", location);

        log.info("Extracting the authorization code");
        UriComponents parseUriComponents = UriComponentsBuilder.fromUriString(
                location.replace("urn:ietf:wg:oauth:2.0:oob:auto", "http://dummy.host")
        ).build();
        List<String> codeList = parseUriComponents.getQueryParams().get("code");
        if (codeList == null || codeList.isEmpty())
            throw new AuthorizationException("Authentication code is not present");
        String code = codeList.get(0);
        log.info("Auth code: {}", code);

        log.info("Getting an operator Access token");
        AuthResponse result = basicAuthorization.login(
                AuthRequest.builder()
                        .grantType(GrantTypeEnum.CODE.getValue())
                        .code(code)
                        .useDefaultRedirectUri()
                        .clientId(dssApiCredConfig.getApiClientId())
                        .build().toMap()
        );

        String operatorToken = result.getAccessToken();
        if (operatorToken == null)
            throw new AuthorizationException("Operator access token is null");
        log.info("Operator access token received");
        log.debug("Operator token: {}", operatorToken);

        if (useCache) {
            Date expireAt = Utils.getFutureDate(result.getExpiresIn());
            operatorAccessTokenRepo.save(
                    OperatorAccessTokenEntity.builder()
                            .token(result.getAccessToken())
                            .expireAt(expireAt)
                            .build()
            );
        }
        return result;
    }
}
