package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.dto.ImageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "image-service", fallbackFactory = ImageServiceFallbackFactory.class)
public interface ImageServiceClient {

    @PostMapping(value = "/api/user-images",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    String createUserImage(ImageDTO imageDTO);

    @PutMapping(value = "/api/user-images",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    String updateUserImage(ImageDTO imageDTO);

    @DeleteMapping(value = "/api/user-images/{filename}")
    void deleteGroupImage(@PathVariable String filename);

}