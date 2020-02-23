package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.UserPrincipal;
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
    public ResponseEntity<UserPrincipal> getUserPrincipalByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserPrincipal userPrincipal = userMapper.userToUserPrincipal(user);
        return ResponseEntity.ok(userPrincipal);
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipal> getUserPrincipalByEmail(@PathVariable String email) {
        User user = userService.getByEmail(email);
        UserPrincipal userPrincipal = userMapper.userToUserPrincipal(user);
        return ResponseEntity.ok(userPrincipal);
    }

    @GetMapping(value = "/email/{email}/nullable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPrincipal> getUserPrincipalByEmailNullable(@PathVariable String email) {
        User user = userService.getByEmailNullable(email);
        UserPrincipal userPrincipal = userMapper.userToUserPrincipal(user);
        return ResponseEntity.ok(userPrincipal);
    }

    @GetMapping(value = "/username/{username}/unique")
    public ResponseEntity<Boolean> isUsernameUnique(@PathVariable("username") String username) {
        boolean isUnique = userService.isUsernameUnique(username);
        return ResponseEntity.ok(isUnique);
    }

    @GetMapping(value = "/email/{email}/unique")
    public ResponseEntity<Boolean> isEmailUnique(@PathVariable("email") String email) {
        boolean isUnique = userService.isEmailUnique(email);
        return ResponseEntity.ok(isUnique);
    }

}
