package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.mapper.UserMapper;
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
import java.util.UUID;

@RestController
@RequestMapping(SystemController.ENDPOINT)
@RequiredArgsConstructor
public class SystemController {
    static final String ENDPOINT = "/api/system";

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(value = "/activate/{userId}")
    public ResponseEntity<Void> activate(@PathVariable UUID userId) {
        userService.activate(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO.getUserId(), resetPasswordDTO.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalById(@PathVariable UUID id) {
        User user = userService.getById(id);
        UserPrincipalDTO userPrincipalDTO = userMapper.pojoToPrincipalDTO(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalByEmail(@PathVariable String email) {
        User user = userService.getByEmail(email);
        UserPrincipalDTO userPrincipalDTO = userMapper.pojoToPrincipalDTO(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserPrincipalDTO userPrincipalDTO = userMapper.pojoToPrincipalDTO(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

    @PostMapping(value = "/local",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> createLocal(@Valid @RequestBody LocalUserDTO localUserDTO) {
        User user = userMapper.localDTOToPojo(localUserDTO);
        user = userService.createLocal(user);
        UserPrincipalDTO userPrincipalDTO = userMapper.pojoToPrincipalDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userPrincipalDTO);
    }

    @PostMapping(value = "/oauth2",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> createOAuth2(@Valid @RequestBody OAuth2UserDTO oAuth2UserDTO) {
        User user = userMapper.oAuth2DTOToPojo(oAuth2UserDTO);
        user = userService.createOAuth2(user);
        UserPrincipalDTO userPrincipalDTO = userMapper.pojoToPrincipalDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userPrincipalDTO);
    }

}
