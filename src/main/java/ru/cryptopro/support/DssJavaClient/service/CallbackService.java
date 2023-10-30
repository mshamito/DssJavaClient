package ru.cryptopro.support.DssJavaClient.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.cryptopro.support.DssJavaClient.dto.confirmation.DssIDPCallbackDto;
import ru.cryptopro.support.DssJavaClient.dto.response.SignResponse;
import ru.cryptopro.support.DssJavaClient.entity.ConfirmationEntity;
import ru.cryptopro.support.DssJavaClient.entity.repos.ConfirmationRepo;
import ru.cryptopro.support.DssJavaClient.enums.OperationStatusEnum;

import java.util.Optional;

@Log4j2
@Service
public class CallbackService {
    private final ConfirmationRepo confirmationRepo;

    public CallbackService(ConfirmationRepo confirmationRepo) {
        this.confirmationRepo = confirmationRepo;
    }

    public void handleSSCallback(SignResponse callback) {
        Optional<ConfirmationEntity> optionalConfirmation = confirmationRepo.findByGuid(callback.getOperation().getId());
        if (optionalConfirmation.isEmpty()) {
            log.error("Confirmation {} not found in embedded database", callback.getOperation().getId());
            return;
        }
        ConfirmationEntity confirmation = optionalConfirmation.get();
        confirmation.setStatus(callback.getOperation().getStatus().name());
        confirmationRepo.save(confirmation);
    }

    public void handleIDPCallback(DssIDPCallbackDto callback) {
        if (callback.getResult().equalsIgnoreCase("success"))
            return;
        Optional<ConfirmationEntity> optionalConfirmation = confirmationRepo.findByGuid(callback.getTransactionId());
        if (optionalConfirmation.isEmpty()) {
            log.error("Confirmation {} not found in embedded database", callback.getTransactionId());
            return;
        }
        ConfirmationEntity confirmation = optionalConfirmation.get();
        confirmation.setStatus(OperationStatusEnum.Error.name());
        confirmation.setError(callback.getError());
        confirmation.setErrorDescription(callback.getErrorDescription());
        confirmationRepo.save(confirmation);
    }
}
