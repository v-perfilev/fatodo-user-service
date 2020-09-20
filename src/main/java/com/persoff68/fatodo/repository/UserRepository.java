package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAllByIdIn(List<String> idList);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsById(String id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
