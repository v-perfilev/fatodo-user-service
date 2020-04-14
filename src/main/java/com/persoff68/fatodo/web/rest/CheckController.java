package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CheckController.ENDPOINT)
@RequiredArgsConstructor
public class CheckController {
    static final String ENDPOINT = "/api/check";

    private final UserService userService;

    @GetMapping(value = "/email/{email}/unique")
    public ResponseEntity<Boolean> isEmailUnique(@PathVariable("email") String email) {
        boolean isUnique = userService.isEmailUnique(email);
        return ResponseEntity.ok(isUnique);
    }

    @GetMapping(value = "/username/{username}/unique")
    public ResponseEntity<Boolean> isUsernameUnique(@PathVariable("username") String username) {
        boolean isUnique = userService.isUsernameUnique(username);
        return ResponseEntity.ok(isUnique);
    }

}
