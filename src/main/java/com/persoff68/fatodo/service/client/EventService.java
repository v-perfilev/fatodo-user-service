package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.EventSystemServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventSystemServiceClient eventSystemServiceClient;

    @Async
    public void deleteAccountPermanently(UUID userId) {
        eventSystemServiceClient.deleteAccountPermanently(userId);
    }
}
