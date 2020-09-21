package com.persoff68.fatodo.repository;

import com.mongodb.lang.NonNull;
import com.persoff68.fatodo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAllByIdIn(List<String> idList);

    @Override
    @NonNull
    Optional<User> findById(@NonNull String id);

    @Override
    @NonNull
    <S extends User> S save(@NonNull S user);

    @Override
    void delete(@NonNull User user);


    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
