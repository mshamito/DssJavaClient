package ru.cryptopro.support.DssJavaClient.entity.repos;

import ru.cryptopro.support.DssJavaClient.entity.OperatorAccessTokenEntity;

import java.util.Optional;

//public interface OperatorAccessTokenRepo extends CrudRepository<OperatorAccessTokenEntity, Long> {
public interface OperatorAccessTokenRepo extends BaseRepo<OperatorAccessTokenEntity, Long> {
    public Optional<OperatorAccessTokenEntity> findFirstByOperator(Long operator);
}
