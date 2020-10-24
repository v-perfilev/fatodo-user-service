package com.persoff68.fatodo.service;

import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.PermissionException;
import com.persoff68.fatodo.service.exception.WrongPasswordException;
import com.persoff68.fatodo.service.exception.WrongProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    public User update(User newUser, byte[] image) {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        if (!userId.equals(newUser.getId())) {
            throw new PermissionException();
        }
        User user = userRepository.findById(newUser.getId())
                .orElseThrow(ModelNotFoundException::new);

        user.setUsername(newUser.getUsername());
        user.setLanguage(newUser.getLanguage());

        String imageFilename = imageService.updateUser(user, newUser, image);
        user.setImageFilename(imageFilename);
        return userRepository.save(user);
    }

    public void changePassword(String oldPassword, String newPassword) {
        UUID id = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        User user = userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
        if (!user.getProvider().equals(Provider.LOCAL)) {
            throw new WrongProviderException();
        }
        if (!isPasswordCorrect(user, oldPassword)) {
            throw new WrongPasswordException();
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private boolean isPasswordCorrect(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

}
