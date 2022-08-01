package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserSummaryDTO;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/username")
    public ResponseEntity<List<String>> getAllUsernamesByIds(@RequestParam("ids") List<UUID> idList) {
        List<User> userList = userService.getAllByIds(idList);
        List<String> usernameList = userList.stream().map(User::getUsername).toList();
        return ResponseEntity.ok(usernameList);
    }

    @GetMapping("/ids/{username}/username-part")
    public ResponseEntity<List<UUID>> getAllIdsByUsernamePart(@PathVariable @NotBlank String username) {
        List<User> userList = userService.getAllByUsernamePart(username);
        List<UUID> userSummaryDTOList = userList.stream().map(User::getId).toList();
        return ResponseEntity.ok(userSummaryDTOList);
    }

    @GetMapping("/summary/{usernamePart}/username-part")
    public ResponseEntity<List<UserSummaryDTO>> getAllByUsernamePart(@PathVariable @NotBlank String usernamePart) {
        List<User> userList = userService.getAllByUsernamePart(usernamePart);
        List<UserSummaryDTO> userSummaryDTOList = userList.stream().map(userMapper::pojoToSummaryDTO).toList();
        return ResponseEntity.ok(userSummaryDTOList);
    }

    @GetMapping("/summary/{username}/username")
    public ResponseEntity<UserSummaryDTO> getByUsername(@PathVariable @NotBlank String username) {
        User user = userService.getByUsername(username);
        UserSummaryDTO userSummaryDTO = userMapper.pojoToSummaryDTO(user);
        return ResponseEntity.ok(userSummaryDTO);
    }

    @GetMapping("/summary/{usernameOrEmail}/username-or-email")
    public ResponseEntity<UserSummaryDTO> getByUsernameOrEmail(@PathVariable @NotBlank String usernameOrEmail) {
        User user = userService.getByUsernameOrEmail(usernameOrEmail);
        UserSummaryDTO userSummaryDTO = userMapper.pojoToSummaryDTO(user);
        return ResponseEntity.ok(userSummaryDTO);
    }

}
