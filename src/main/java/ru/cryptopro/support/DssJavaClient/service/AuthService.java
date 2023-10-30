package ru.cryptopro.support.DssJavaClient.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.cryptopro.support.DssJavaClient.auth.interfaces.Authorization;
import ru.cryptopro.support.DssJavaClient.dto.common.UserCredentialDto;
import ru.cryptopro.support.DssJavaClient.dto.response.AuthResponse;
import ru.cryptopro.support.DssJavaClient.entity.DelegatedTokenEntity;
import ru.cryptopro.support.DssJavaClient.entity.SimpleTokenEntity;
import ru.cryptopro.support.DssJavaClient.entity.interfaces.AccessToken;
import ru.cryptopro.support.DssJavaClient.entity.repos.DelegatedTokenRepo;
import ru.cryptopro.support.DssJavaClient.entity.repos.SimpleTokenRepo;
import ru.cryptopro.support.DssJavaClient.util.Utils;

import java.util.Date;
import java.util.Optional;

@Log4j2
@Service

public class AuthService {
    private final Authorization authorization;
    private final Authorization operatorAuthorization;
    private final SimpleTokenRepo simpleTokenRepo;
    private final DelegatedTokenRepo delegatedTokenRepo;
    @Value("${dss.api_client.use_cache:true}")
    private boolean useCache;

    public AuthService(
            Authorization authorization,
            @Qualifier("OperatorCertAuthorization")
            Authorization operatorAuthorization,
            SimpleTokenRepo simpleTokenRepo,
            DelegatedTokenRepo delegatedTokenRepo) {
        this.authorization = authorization;
        this.operatorAuthorization = operatorAuthorization;
        this.simpleTokenRepo = simpleTokenRepo;
        this.delegatedTokenRepo = delegatedTokenRepo;
    }

    public AuthResponse login(UserCredentialDto userCredential) {

        // operator auth for UMS
        if (userCredential.retrieveOperatorTokenOnly()) {
            // request & save to db
            return operatorAuthorization.login(userCredential);
        }

        // user auth
        String username = userCredential.getUsername();

        if (useCache) {
            Optional<? extends AccessToken> optionalCachedToken =
                    userCredential.isUsingOperatorCertForAuth() ?
                            delegatedTokenRepo.findFirstByUsername(username)
                            : simpleTokenRepo.findFirstByUsername(username);

            if (optionalCachedToken.isPresent()) {
                AccessToken cachedToken = optionalCachedToken.get();
                AuthResponse cached = new AuthResponse(cachedToken);
                log.info("Cached token returned for user {}, expire at: {}", username, cachedToken.getExpireAt());
                return cached;
            }
        }

        // login request & save to db
        if (userCredential.isUsingOperatorCertForAuth()) { // getting delegated token
            AuthResponse result = operatorAuthorization.login(userCredential);
            Date expireAt = Utils.getFutureDate(result.getExpiresIn());
            if (useCache)
                delegatedTokenRepo.save(
                        DelegatedTokenEntity.builder()
                                .token(result.getAccessToken())
                                .expireAt(expireAt)
                                .username(username)
                                .build()
                );
            log.info("Received delegated token for user: {}, expire at: {}", username, expireAt);
            return result;
        } else { // getting simple token
            AuthResponse result = authorization.login(userCredential);
            Date expireAt = Utils.getFutureDate(result.getExpiresIn());
            if (useCache)
                simpleTokenRepo.save(
                        SimpleTokenEntity.builder()
                                .token(result.getAccessToken())
                                .expireAt(expireAt)
                                .username(username)
                                .build()
                );
            log.info("Received token for user: {}, expire at: {}", username, expireAt);
            return result;
        }
    }
}
