package com.persoff68.fatodo.service;

import com.persoff68.fatodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailService {

    private final UserRepository userRepository;

}
