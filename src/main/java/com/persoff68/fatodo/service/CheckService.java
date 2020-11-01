package com.persoff68.fatodo.service;

import com.persoff68.fatodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final UserRepository userRepository;

    public boolean doesUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean doesEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean doesIdExist(UUID id) {
        return userRepository.existsById(id);
    }

}
