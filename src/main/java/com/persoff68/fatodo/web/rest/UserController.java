package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.mapper.UserMapper;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UserController.ENDPOINT)
@RequiredArgsConstructor
public class UserController {
    static final String ENDPOINT = "/api/user";

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getCurrentUser() {
        String id = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        User user = userService.getById(id);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping(value = "all-by-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllByIds(@RequestBody List<String> idList) {
        List<User> userList = userService.getAllByIds(idList);
        List<UserDTO> userDTOList = userList.stream()
                .map(userMapper::userToUserDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOList);
    }

}
