package ru.cryptopro.support.DssJavaClient.entity.repos;

import ru.cryptopro.support.DssJavaClient.entity.ConfirmationEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfirmationRepo extends BaseRepo<ConfirmationEntity, UUID> {
    Optional<ConfirmationEntity> findByGuid(UUID id);
    List<ConfirmationEntity> findByUsername(String username);
}
