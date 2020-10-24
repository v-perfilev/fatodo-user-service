package com.persoff68.fatodo.service;

import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Language;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.service.exception.ModelAlreadyExistsException;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> getAllByIds(List<UUID> idList) {
        return userRepository.findAllByIdIn(idList);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User createLocal(User user) {
        if (user.getId() != null) {
            throw new ModelAlreadyExistsException();
        }
        user.setProvider(Provider.LOCAL);
        return create(user);
    }

    public User createOAuth2(User user) {
        if (user.getId() != null) {
            throw new ModelAlreadyExistsException();
        }
        user.setActivated(true);
        return create(user);
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User create(User user) {
        String language = user.getLanguage();
        if (language == null || !Language.contains(language)) {
            user.setLanguage(Language.DEFAULT.getValue());
        }
        user.setAuthorities(Set.of(new Authority(AuthorityType.USER.getValue())));
        return userRepository.save(user);
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            throw new ModelInvalidException();
        }
        User user = userRepository.findById(newUser.getId())
                .orElseThrow(ModelNotFoundException::new);

        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());
        user.setLanguage(newUser.getLanguage());

        return userRepository.save(user);
    }

    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
        userRepository.delete(user);
    }

    public void activate(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(ModelNotFoundException::new);
        if (user.isActivated()) {
            throw new UserAlreadyActivatedException();
        }
        user.setActivated(true);
        userRepository.save(user);
    }

    public void resetPassword(UUID userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(ModelNotFoundException::new);
        user.setPassword(password);
        userRepository.save(user);
    }

}
