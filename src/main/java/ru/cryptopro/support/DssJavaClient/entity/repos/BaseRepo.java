package ru.cryptopro.support.DssJavaClient.entity.repos;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import ru.cryptopro.support.DssJavaClient.entity.interfaces.Expirable;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepo <T, ID extends Serializable> extends Repository<T, ID> {
    public Iterable<Expirable> findAll();
    T save(T persistent);
    void delete(Expirable token);
}
