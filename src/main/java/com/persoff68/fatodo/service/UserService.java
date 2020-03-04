package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.service.exception.ModelAlreadyExistsException;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
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

    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User create(User user) {
        String id = user.getId();
        if (id != null) {
            throw new ModelAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    public User update(User user) {
        String id = user.getId();
        userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
        return userRepository.save(user);
    }

    public void delete(String id) {
        userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
        userRepository.deleteById(id);
    }

    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

}
