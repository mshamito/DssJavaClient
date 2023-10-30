package ru.cryptopro.support.DssJavaClient.entity.repos;

import ru.cryptopro.support.DssJavaClient.entity.DelegatedTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface DelegatedTokenRepo extends BaseRepo<DelegatedTokenEntity, UUID> {
    Optional<DelegatedTokenEntity> findFirstByUsername(String username);
}
