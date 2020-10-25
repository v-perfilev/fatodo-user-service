package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.ImageDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class ImageServiceClientWrapper implements ImageServiceClient {

    @Qualifier("imageServiceClient")
    private final ImageServiceClient imageServiceClient;

    @Override
    public String createUserImage(ImageDTO imageDTO) {
        try {
            return imageServiceClient.createUserImage(imageDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public String updateUserImage(ImageDTO imageDTO) {
        try {
            return imageServiceClient.updateUserImage(imageDTO);
        } catch (FeignException.NotFound e) {
            return imageServiceClient.createUserImage(imageDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void deleteUserImage(String filename) {
        try {
            imageServiceClient.deleteUserImage(filename);
        } catch (FeignException.NotFound e) {
            // skip
        } catch (Exception e) {
            throw new ClientException();
        }
    }

}
