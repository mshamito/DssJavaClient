package ru.cryptopro.support.DssJavaClient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.cryptopro.support.DssJavaClient.entity.interfaces.Expirable;
import ru.cryptopro.support.DssJavaClient.entity.repos.*;
import ru.cryptopro.support.DssJavaClient.util.Utils;

@Service
@RequiredArgsConstructor
public class CleanUpService {
    private final SimpleTokenRepo simpleTokenRepo;
    private final DelegatedTokenRepo delegatedTokenRepo;
    private final OperatorAccessTokenRepo operatorAccessTokenRepo;
    private final ConfirmationRepo confirmationRepo;
    private final long TIMEOUT_SCHEDULED = 5 * 1000;

    // time, when token recognized as expiring
    // expireAt < current time + timeoutSkew
    private final int TIME_SKEW = 10;


    private void cleanUp(BaseRepo<?,?> repository) {
        Iterable<Expirable> tokens = repository.findAll();
        for (Expirable token : tokens) {
            if (token.getExpireAt().before(Utils.getFutureDate(TIME_SKEW))) { // token expired or will expire soon
                repository.delete(token);
            }
        }
    }
    @Scheduled(fixedRate = TIMEOUT_SCHEDULED)
    public void cleanUp() {
        cleanUp(simpleTokenRepo);
        cleanUp(operatorAccessTokenRepo);
        cleanUp(delegatedTokenRepo);
        cleanUp(confirmationRepo);
    }
}
