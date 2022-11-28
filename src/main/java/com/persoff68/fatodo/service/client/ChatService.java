package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.ChatSystemServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatSystemServiceClient chatSystemServiceClient;

    @Async
    public void deleteAccountPermanently(UUID userId) {
        chatSystemServiceClient.deleteAccountPermanently(userId);
    }
}
