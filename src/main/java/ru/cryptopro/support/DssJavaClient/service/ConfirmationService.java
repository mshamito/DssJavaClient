package ru.cryptopro.support.DssJavaClient.service;

import org.springframework.stereotype.Service;
import ru.cryptopro.support.DssJavaClient.entity.ConfirmationEntity;
import ru.cryptopro.support.DssJavaClient.entity.repos.ConfirmationRepo;

import java.util.List;

@Service
public class ConfirmationService {
    private final ConfirmationRepo confirmationRepo;

    public ConfirmationService(ConfirmationRepo confirmationRepo) {
        this.confirmationRepo = confirmationRepo;
    }

    public List<ConfirmationEntity> getConfirmationsForUser(String username) {
        return confirmationRepo.findByUsername(username);
    }
}
