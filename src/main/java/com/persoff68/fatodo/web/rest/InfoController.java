package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserInfoDTO;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping(value = "/user")
    public ResponseEntity<List<UserInfoDTO>> getAllUserInfoByIds(@RequestParam("ids") List<UUID> idList) {
        List<User> userList = userService.getAllByIds(idList);
        List<UserInfoDTO> userInfoDTOList = userList.stream()
                .map(userMapper::pojoToInfoDTO)
                .toList();
        return ResponseEntity.ok(userInfoDTOList);
    }

}
