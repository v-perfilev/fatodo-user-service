package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.ContactSystemServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactSystemServiceClient contactSystemServiceClient;

    @Async
    public void deleteAccountPermanently(UUID userId) {
        contactSystemServiceClient.deleteAccountPermanently(userId);
    }
}
