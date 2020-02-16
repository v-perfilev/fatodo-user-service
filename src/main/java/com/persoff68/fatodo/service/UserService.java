package com.persoff68.fatodo.service;

import com.persoff68.fatodo.exception.RecordNotFoundException;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
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
                .orElseThrow(RecordNotFoundException::new);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(RecordNotFoundException::new);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(RecordNotFoundException::new);
    }

    public User getByEmailIfExists(String email) {
        return userRepository.findByEmail(email)
                .orElse(new User());
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        String id = user.getId();
        userRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
        return userRepository.save(user);
    }

    public void delete(String id) {
        userRepository.findById(id)
                .orElseThrow(RecordNotFoundException::new);
        userRepository.deleteById(id);
    }

}
