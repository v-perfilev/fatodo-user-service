package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.ImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageServiceClient imageServiceClient;

    public String updateUser(User oldUser, User newUser, byte[] image) {
        String oldFilename = oldUser.getImageFilename();
        String newFilename = newUser.getImageFilename();
        String filename = null;
        if (image != null && oldFilename == null) {
            ImageDTO imageDTO = new ImageDTO(null, image);
            filename = imageServiceClient.createUserImage(imageDTO);
        } else if (image != null) {
            ImageDTO imageDTO = new ImageDTO(oldFilename, image);
            filename = imageServiceClient.updateUserImage(imageDTO);
        } else if (oldFilename != null && newFilename == null) {
            imageServiceClient.deleteUserImage(oldFilename);
        }
        return filename;
    }
}
