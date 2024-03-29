package com.persoff68.fatodo.service;

import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.constant.Provider;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.service.exception.ModelAlreadyExistsException;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import com.persoff68.fatodo.service.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final int DEFAULT_SEARCH_SIZE = 10;

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> getAllByIds(List<UUID> idList) {
        return userRepository.findAllByIdIn(idList);
    }

    public List<User> getAllByUsernamePart(String username) {
        PageRequest pageRequest = PageRequest.of(0, DEFAULT_SEARCH_SIZE);
        Page<User> userPage = userRepository.findAllByUsernameStartsWithIgnoreCase(username, pageRequest);
        return userPage.toList();
    }

    public User getByUsernameOrEmail(String usernameOrEmail) {
        boolean isEmail = UserUtils.isEmail(usernameOrEmail);
        User user = null;
        if (isEmail) {
            user = userRepository.findByEmailIgnoreCase(usernameOrEmail).orElse(null);
        }
        if (user == null) {
            user = userRepository.findByUsernameIgnoreCase(usernameOrEmail).orElse(null);
        }
        if (user == null) {
            throw new ModelNotFoundException();
        }
        return user;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(ModelNotFoundException::new);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
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
        user.setEmail(user.getEmail().toLowerCase());
        user.setAuthorities(Set.of(new Authority(AuthorityType.USER.getValue())));
        return userRepository.save(user);
    }

    public User update(User newUser) {
        UUID newUserId = newUser.getId();
        if (newUserId == null) {
            throw new ModelInvalidException();
        }
        User user = userRepository.findById(newUserId)
                .orElseThrow(ModelNotFoundException::new);

        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());

        user.setInfo(newUser.getInfo());

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
