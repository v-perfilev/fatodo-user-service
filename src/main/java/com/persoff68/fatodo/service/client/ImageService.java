package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.model.Info;
import com.persoff68.fatodo.model.dto.ImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageServiceClient imageServiceClient;

    public String updateUser(Info oldInfo, Info newInfo, byte[] image) {
        String filename = oldInfo.getImageFilename();
        String newFilename = newInfo.getImageFilename();
        if (image != null && filename == null) {
            ImageDTO imageDTO = new ImageDTO(null, image);
            filename = imageServiceClient.createUserImage(imageDTO);
        } else if (image != null) {
            ImageDTO imageDTO = new ImageDTO(filename, image);
            filename = imageServiceClient.updateUserImage(imageDTO);
        } else if (filename != null && newFilename == null) {
            imageServiceClient.deleteUserImage(filename);
        }
        return filename;
    }

    @Async
    public void deleteUserImage(String filename) {
        imageServiceClient.deleteUserImage(filename);
    }
}
