package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.client.ChatSystemServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:chatservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class ChatServiceCT {

    @Autowired
    ChatSystemServiceClient chatSystemServiceClient;

    @Test
    @WithCustomSecurityContext
    void testCreateGroupImage() {
        assertDoesNotThrow(() -> chatSystemServiceClient.deleteAccountPermanently(UUID.randomUUID()));

    }

}
