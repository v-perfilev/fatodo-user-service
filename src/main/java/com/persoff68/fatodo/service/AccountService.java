package com.persoff68.fatodo.service;

import com.persoff68.fatodo.config.constant.Language;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.client.ImageService;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
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

    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User update(User newUser, byte[] image) {
        UUID currentUserId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        UUID newUserId = newUser.getId();
        if (newUserId == null) {
            throw new ModelInvalidException();
        }
        if (!currentUserId.equals(newUser.getId())) {
            throw new PermissionException();
        }
        User user = userRepository.findById(newUserId).orElseThrow(ModelNotFoundException::new);

        user.setUsername(newUser.getUsername());
        user.setInfo(newUser.getInfo());

        String imageFilename = imageService.updateUser(user, newUser, image);
        user.getInfo().setImageFilename(imageFilename);

        return userRepository.save(user);
    }

    public void changePassword(String oldPassword, String newPassword) {
        UUID id = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        User user = userRepository.findById(id).orElseThrow(ModelNotFoundException::new);
        if (!user.getProvider().equals(Provider.LOCAL)) {
            throw new WrongProviderException();
        }
        if (!isPasswordCorrect(user, oldPassword)) {
            throw new WrongPasswordException();
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeLanguage(String languageString) {
        UUID id = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        User user = userRepository.findById(id).orElseThrow(ModelNotFoundException::new);
        Language language = Language.valueOf(languageString);
        user.getInfo().setLanguage(language);
        userRepository.save(user);
    }

    private boolean isPasswordCorrect(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

}
