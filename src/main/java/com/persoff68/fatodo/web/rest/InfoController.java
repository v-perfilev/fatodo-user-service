package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserInfoDTO;
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
@RequestMapping(InfoController.ENDPOINT)
@RequiredArgsConstructor
public class InfoController {
    static final String ENDPOINT = "/api/info";

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserInfoDTO>> getAllUserInfoByIds(@RequestParam("ids") List<UUID> idList) {
        List<User> userList = userService.getAllByIds(idList);
        List<UserInfoDTO> userInfoDTOList = userList.stream()
                .map(userMapper::pojoToInfoDTO)
                .toList();
        return ResponseEntity.ok(userInfoDTOList);
    }

    @GetMapping("/{usernamePart}/username-part")
    public ResponseEntity<List<UserInfoDTO>> getAllByUsernamePart(@PathVariable @NotBlank String usernamePart) {
        List<User> userList = userService.getAllByUsernamePart(usernamePart);
        List<UserInfoDTO> userInfoDTOList = userList.stream()
                .map(userMapper::pojoToInfoDTO)
                .toList();
        return ResponseEntity.ok(userInfoDTOList);
    }

    @GetMapping("/{username}/username")
    public ResponseEntity<UserInfoDTO> getByUsername(@PathVariable @NotBlank String username) {
        User user = userService.getByUsername(username);
        UserInfoDTO userInfoDTO = userMapper.pojoToInfoDTO(user);
        return ResponseEntity.ok(userInfoDTO);
    }

    @GetMapping("/{usernameOrEmail}/username-or-email")
    public ResponseEntity<UserInfoDTO> getByUsernameOrEmail(@PathVariable @NotBlank String usernameOrEmail) {
        User user = userService.getByUsernameOrEmail(usernameOrEmail);
        UserInfoDTO userInfoDTO = userMapper.pojoToInfoDTO(user);
        return ResponseEntity.ok(userInfoDTO);
    }

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


}
