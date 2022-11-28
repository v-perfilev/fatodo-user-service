package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ItemSystemServiceClientWrapper implements ItemSystemServiceClient {

    @Qualifier("feignItemSystemServiceClient")
    private final ItemSystemServiceClient itemSystemServiceClient;

    @Override
    public void deleteAccountPermanently(UUID userId) {
        try {
            itemSystemServiceClient.deleteAccountPermanently(userId);
        } catch (Exception e) {
            throw new ClientException();
        }
    }
}
