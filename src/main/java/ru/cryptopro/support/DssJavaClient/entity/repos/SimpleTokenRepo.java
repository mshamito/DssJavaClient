package ru.cryptopro.support.DssJavaClient.entity.repos;

import ru.cryptopro.support.DssJavaClient.entity.SimpleTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface SimpleTokenRepo extends BaseRepo<SimpleTokenEntity, UUID> {
    Optional<SimpleTokenEntity> findFirstByUsername(String username);
}