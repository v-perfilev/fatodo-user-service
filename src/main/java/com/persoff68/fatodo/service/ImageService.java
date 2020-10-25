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
        String filename = oldUser.getImageFilename();
        String newFilename = newUser.getImageFilename();
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
}
