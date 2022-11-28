package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventSystemServiceClientWrapper implements EventSystemServiceClient {

    @Qualifier("feignEventSystemServiceClient")
    private final EventSystemServiceClient eventSystemServiceClient;

    @Override
    public void deleteAccountPermanently(UUID userId) {
        try {
            eventSystemServiceClient.deleteAccountPermanently(userId);
        } catch (Exception e) {
            throw new ClientException();
        }
    }
}
