package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.client.ItemSystemServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:itemservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class ItemServiceCT {

    @Autowired
    ItemSystemServiceClient itemSystemServiceClient;

    @Test
    @WithCustomSecurityContext
    void testCreateGroupImage() {
        assertDoesNotThrow(() -> itemSystemServiceClient.deleteAccountPermanently(UUID.randomUUID()));

    }

}
