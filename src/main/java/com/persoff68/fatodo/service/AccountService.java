package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Info;
import com.persoff68.fatodo.model.Settings;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.constant.Language;
import com.persoff68.fatodo.model.constant.Provider;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.client.ChatService;
import com.persoff68.fatodo.service.client.ContactService;
import com.persoff68.fatodo.service.client.EventService;
import com.persoff68.fatodo.service.client.ImageService;
import com.persoff68.fatodo.service.client.ItemService;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
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
    private final ContactService contactService;
    private final ItemService itemService;
    private final ChatService chatService;
    private final EventService eventService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void updateInfo(String username, Info info, byte[] image) {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        User user = userRepository.findById(userId)
                .orElseThrow(ModelNotFoundException::new);

        String imageFilename = imageService.updateUser(user.getInfo(), info, image);

        user.setUsername(username);
        user.setInfo(info);
        user.getInfo().setImageFilename(imageFilename);

        userRepository.save(user);
    }

    public void updateSettings(Settings settings) {
        UUID userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        User user = userRepository.findById(userId)
                .orElseThrow(ModelNotFoundException::new);

        user.setSettings(settings);

        userRepository.save(user);
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

    public void changeLanguage(String language) {
        UUID id = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        User user = userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
        Language languageValue = Language.valueOf(language);
        user.getSettings().setLanguage(languageValue);
        userRepository.save(user);
    }

    public void deleteAccountPermanently() {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        User user = userRepository.findById(userId)
                .orElseThrow(ModelNotFoundException::new);

        // remove image
        String imageFilename = user.getInfo().getImageFilename();
        if (imageFilename != null) {
            imageService.deleteUserImage(imageFilename);
        }

        // remove traces in other services
        contactService.deleteAccountPermanently(userId);
        itemService.deleteAccountPermanently(userId);
        chatService.deleteAccountPermanently(userId);
        eventService.deleteAccountPermanently(userId);

        // clear user object
        String idString = userId.toString();
        user.setEmail(idString);
        user.setUsername(idString);
        user.setPassword(idString);
        user.setDeleted(true);
        user.setInfo(new Info());
        userRepository.save(user);

    }

    private boolean isPasswordCorrect(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

}
