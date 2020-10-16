package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.PermissionException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    public User update(User newUser, byte[] image) {
        String userId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
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

}
