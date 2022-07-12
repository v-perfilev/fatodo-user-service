package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserInfoDTO;
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

@RestController
@RequestMapping(InfoController.ENDPOINT)
@RequiredArgsConstructor
public class InfoController {
    static final String ENDPOINT = "/api/info";

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserSummaryDTO>> getAllUserSummaryByIds(@RequestBody List<UUID> idList) {
        List<User> userList = userService.getAllByIds(idList);
        List<UserSummaryDTO> userSummaryDTOList = userList.stream()
                .map(userMapper::pojoToSummaryDTO)
                .toList();
        return ResponseEntity.ok(userSummaryDTOList);
    }

    @PostMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserInfoDTO>> getAllUserInfoByIds(@RequestBody List<UUID> idList) {
        List<User> userList = userService.getAllByIds(idList);
        List<UserInfoDTO> userInfoDTOList = userList.stream()
                .map(userMapper::pojoToInfoDTO)
                .toList();
        return ResponseEntity.ok(userInfoDTOList);
    }

}
