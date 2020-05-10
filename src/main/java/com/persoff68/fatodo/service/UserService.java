package com.persoff68.fatodo.service;

import com.persoff68.fatodo.config.aop.cache.annotation.RedisCacheEvict;
import com.persoff68.fatodo.config.aop.cache.annotation.RedisCacheable;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.service.exception.ModelAlreadyExistsException;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @RedisCacheable(cacheName = "users", key = "#id")
    public User getById(String id) {

        return userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User create(User user) {
        if (user.getId() != null) {
            throw new ModelAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    @RedisCacheEvict(cacheName = "users", key = "#user.id")
    public User update(User user) {
        if (user.getId() == null) {
            throw new ModelInvalidException();
        }
        if (!userRepository.existsById(user.getId())) {
            throw new ModelNotFoundException();
        }
        return userRepository.save(user);
    }

    @RedisCacheEvict(cacheName = "users", key = "#id")
    public void delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new ModelNotFoundException();
        }
        userRepository.deleteById(id);
    }

    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

    public void activate(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
        if (user.isActivated()) {
            throw new UserAlreadyActivatedException();
        }
        user.setActivated(true);
        userRepository.save(user);
    }

    public void resetPassword(String userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(ModelNotFoundException::new);
        user.setPassword(password);
        userRepository.save(user);
    }

}
