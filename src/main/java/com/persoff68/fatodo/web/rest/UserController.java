package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserSummaryDTO;
import com.persoff68.fatodo.model.mapper.UserMapper;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UserController.ENDPOINT)
@RequiredArgsConstructor
public class UserController {
    static final String ENDPOINT = "/api/user";

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(value = "/all-by-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSummaryDTO>> getAllByIds(@RequestBody List<UUID> idList) {
        List<User> userList = userService.getAllByIds(idList);
        List<UserSummaryDTO> userSummaryDTOList = userList.stream()
                .map(userMapper::pojoToSummaryDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userSummaryDTOList);
    }

}
