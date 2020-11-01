package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(CheckController.ENDPOINT)
@RequiredArgsConstructor
public class CheckController {
    static final String ENDPOINT = "/api/check";

    private final CheckService checkService;

    @GetMapping(value = "/email-exists/{email}")
    public ResponseEntity<Boolean> doesEmailExist(@PathVariable String email) {
        boolean emailExists = checkService.doesEmailExist(email);
        return ResponseEntity.ok(emailExists);
    }

    @GetMapping(value = "/username-exists/{username}")
    public ResponseEntity<Boolean> doesUsernameExist(@PathVariable String username) {
        boolean usernameExists = checkService.doesUsernameExist(username);
        return ResponseEntity.ok(usernameExists);
    }

    @GetMapping(value = "/id-exists/{id}")
    public ResponseEntity<Boolean> doesIdExist(@PathVariable UUID id) {
        boolean idExists = checkService.doesIdExist(id);
        return ResponseEntity.ok(idExists);
    }

}
