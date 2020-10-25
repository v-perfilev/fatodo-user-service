package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.model.dto.ImageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:imageservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
public class ImageServiceCT {

    @Autowired
    ImageServiceClient imageServiceClient;

    @Test
    void testCreateGroupImage() {
        byte[] bytes = new byte[100];
        Arrays.fill(bytes, (byte) 41);
        ImageDTO imageDTO = new ImageDTO(null, bytes);
        String filename = imageServiceClient.createUserImage(imageDTO);
        assertThat(filename).isNotBlank();
    }

    @Test
    void testUpdateGroupImage() {
        byte[] bytes = new byte[100];
        Arrays.fill(bytes, (byte) 41);
        ImageDTO imageDTO = new ImageDTO("test_filename", bytes);
        String filename = imageServiceClient.updateUserImage(imageDTO);
        assertThat(filename).isNotBlank();
    }

    @Test
    void testDeleteGroupImage() {
        imageServiceClient.deleteUserImage("test_filename");
        assertThat(true).isTrue();
    }

}
