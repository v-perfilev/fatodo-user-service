package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(AuthController.ENDPOINT)
@RequiredArgsConstructor
public class AuthController {
    static final String ENDPOINT = "/auth";

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalById(@PathVariable String id) {
        User user = userService.getById(id);
        UserPrincipalDTO userPrincipalDTO = userMapper.userToUserPrincipalDTO(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalByEmail(@PathVariable String email) {
        User user = userService.getByEmail(email);
        UserPrincipalDTO userPrincipalDTO = userMapper.userToUserPrincipalDTO(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserPrincipalDTO userPrincipalDTO = userMapper.userToUserPrincipalDTO(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

    @PostMapping(value = "/local", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createLocal(@Valid @RequestBody LocalUserDTO localUserDTO) {
        User user = userMapper.localUserDTOToUser(localUserDTO);
        user.setProvider(Provider.LOCAL);
        user = userService.create(user);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping(value = "/oauth2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createOAuth2(@Valid @RequestBody OAuth2UserDTO oAuth2UserDTO) {
        User user = userMapper.oAuth2UserDTOToUser(oAuth2UserDTO);
        user = userService.create(user);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

}
