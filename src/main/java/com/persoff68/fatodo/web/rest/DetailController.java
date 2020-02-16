package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DetailController.ENDPOINT)
@RequiredArgsConstructor
public class DetailController {

    static final String ENDPOINT = "/details";

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserPrincipalDTO userPrincipalDTO = userMapper.userToUserPrincipalDto(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalByEmail(@PathVariable String email) {
        User user = userService.getByEmail(email);
        UserPrincipalDTO userPrincipalDTO = userMapper.userToUserPrincipalDto(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

    @GetMapping(value = "/email-if-exists/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipalDTO> getUserPrincipalByEmailOrNew(@PathVariable String email) {
        User user = userService.getByEmailIfExists(email);
        UserPrincipalDTO userPrincipalDTO = userMapper.userToUserPrincipalDto(user);
        return ResponseEntity.ok(userPrincipalDTO);
    }

}
