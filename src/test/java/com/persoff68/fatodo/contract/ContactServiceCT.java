package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.client.ContactSystemServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:contactservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class ContactServiceCT {

    @Autowired
    ContactSystemServiceClient contactSystemServiceClient;

    @Test
    @WithCustomSecurityContext
    void testCreateGroupImage() {
        assertDoesNotThrow(() -> contactSystemServiceClient.deleteAccountPermanently(UUID.randomUUID()));

    }

}
