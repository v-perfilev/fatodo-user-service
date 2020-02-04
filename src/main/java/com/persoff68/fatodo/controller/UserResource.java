package com.persoff68.fatodo.controller;

import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserResource.ENDPOINT)
@RequiredArgsConstructor
public class UserResource {

    static final String ENDPOINT = "/api/v1/user";

    private final UserService userService;
    private final UserMapper userMapper;


}
