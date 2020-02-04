package com.persoff68.fatodo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DetailController.ENDPOINT)
@RequiredArgsConstructor
public class DetailController {

    static final String ENDPOINT = "/api/v1/detail";


}
