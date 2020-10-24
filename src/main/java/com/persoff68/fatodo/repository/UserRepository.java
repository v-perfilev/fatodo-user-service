package com.persoff68.fatodo.repository;

import com.mongodb.lang.NonNull;
import com.persoff68.fatodo.config.aop.cache.annotation.CacheEvictMethod;
import com.persoff68.fatodo.config.aop.cache.annotation.CacheableMethod;
import com.persoff68.fatodo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {

    @CacheableMethod(cacheName = "users-by-id-list", keyCacheName = "users-by-id-list-keys", key = "#idList")
    List<User> findAllByIdIn(List<UUID> idList);

    @Override
    @CacheableMethod(cacheName = "users-by-id", key = "#id")
    @NonNull
    Optional<User> findById(@NonNull UUID id);

    @Override
    @CacheEvictMethod(cacheName = "users-by-id", key = "#user.id")
    @CacheEvictMethod(cacheName = "users-by-id-list", keyCacheName = "users-by-id-list-keys", key = "#user.id")
    @NonNull
    <S extends User> S save(@NonNull S user);

    @Override
    @CacheEvictMethod(cacheName = "users-by-id", key = "#user.id")
    @CacheEvictMethod(cacheName = "users-by-id-list", keyCacheName = "users-by-id-list-keys", key = "#user.id")
    void delete(@NonNull User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
