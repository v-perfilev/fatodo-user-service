package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.service.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final UserRepository userRepository;

    public boolean doesUsernameExist(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean doesEmailExist(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public boolean doesUsernameOrEmailExist(String user) {
        boolean isEmail = UserUtils.isEmail(user);
        boolean usernameOrEmailExists = false;
        if (isEmail) {
            usernameOrEmailExists = userRepository.existsByEmailIgnoreCase(user);
        }
        if (!usernameOrEmailExists) {
            usernameOrEmailExists = userRepository.existsByUsernameIgnoreCase(user);
        }
        return usernameOrEmailExists;
    }

    public boolean doesIdExist(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return false;
        } else {
            return !userOptional.get().isDeleted();
        }
    }

    public boolean doIdsExist(List<UUID> idList) {
        List<User> userList = userRepository.findAllByIdIn(idList);
        List<User> allowedUserList = userList.stream()
                .filter(u -> !u.isDeleted())
                .toList();
        return allowedUserList.size() == idList.size();
    }

}
