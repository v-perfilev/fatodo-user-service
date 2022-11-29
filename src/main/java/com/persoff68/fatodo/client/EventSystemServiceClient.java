package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignSystemConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "event-service", primary = false,
        configuration = {FeignSystemConfiguration.class},
        qualifiers = {"feignEventSystemServiceClient"})
public interface EventSystemServiceClient {

    @DeleteMapping(value = "/api/system/{userId}")
    void deleteAccountPermanently(@PathVariable UUID userId);

}
