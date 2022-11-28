package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.ItemSystemServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemSystemServiceClient itemSystemServiceClient;

    @Async
    public void deleteAccountPermanently(UUID userId) {
        itemSystemServiceClient.deleteAccountPermanently(userId);
    }
}
