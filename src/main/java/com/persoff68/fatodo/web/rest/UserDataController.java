package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserSummaryDTO;
import com.persoff68.fatodo.model.mapper.UserMapper;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(UserDataController.ENDPOINT)
@RequiredArgsConstructor
public class UserDataController {
    static final String ENDPOINT = "/api/user-data";

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(value = "/usernames/ids", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllUsernamesByIds(@RequestBody List<UUID> idList) {
        List<User> userList = userService.getAllByIds(idList);
        List<String> usernameList = userList.stream()
                .map(User::getUsername)
                .toList();
        return ResponseEntity.ok(usernameList);
    }

    @GetMapping(value = "/all/{username}/username-part", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSummaryDTO>> getAllByUsernamePart(@PathVariable @NotBlank String username) {
        List<User> userList = userService.getAllByUsernamePart(username);
        List<UserSummaryDTO> userSummaryDTOList = userList.stream()
                .map(userMapper::pojoToSummaryDTO)
                .toList();
        return ResponseEntity.ok(userSummaryDTOList);
    }

    @GetMapping(value = "/ids/{username}/username-part", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UUID>> getAllIdsByUsernamePart(@PathVariable @NotBlank String username) {
        List<User> userList = userService.getAllByUsernamePart(username);
        List<UUID> userSummaryDTOList = userList.stream()
                .map(User::getId)
                .toList();
        return ResponseEntity.ok(userSummaryDTOList);
    }

    @GetMapping(value = "/{username}/username", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSummaryDTO> getByUsername(@PathVariable @NotBlank String username) {
        User user = userService.getByUsername(username);
        UserSummaryDTO userSummaryDTO = userMapper.pojoToSummaryDTO(user);
        return ResponseEntity.ok(userSummaryDTO);
    }

    @GetMapping(value = "/{usernameOrEmail}/username-or-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSummaryDTO> getByUsernameOrEmail(@PathVariable @NotBlank String usernameOrEmail) {
        User user = userService.getByUsernameOrEmail(usernameOrEmail);
        UserSummaryDTO userSummaryDTO = userMapper.pojoToSummaryDTO(user);
        return ResponseEntity.ok(userSummaryDTO);
    }

}
