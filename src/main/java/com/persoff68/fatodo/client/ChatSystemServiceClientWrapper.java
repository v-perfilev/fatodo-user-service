package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatSystemServiceClientWrapper implements ChatSystemServiceClient {

    @Qualifier("feignChatSystemServiceClient")
    private final ChatSystemServiceClient chatSystemServiceClient;

    @Override
    public void deleteAccountPermanently(UUID userId) {
        try {
            chatSystemServiceClient.deleteAccountPermanently(userId);
        } catch (Exception e) {
            throw new ClientException();
        }
    }
}
