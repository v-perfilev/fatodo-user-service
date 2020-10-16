package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.ImageDTO;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ImageServiceFallbackFactory implements FallbackFactory<ImageServiceClient> {
    @Override
    public ImageServiceClient create(Throwable throwable) {

        int status = throwable instanceof FeignException
                ? ((FeignException) throwable).status()
                : 0;

        return new ImageServiceClient() {
            @Override
            public String createUserImage(ImageDTO imageDTO) {
                throw new ClientException();
            }

            @Override
            public String updateUserImage(ImageDTO imageDTO) {
                if (status == 404) {
                    throw new ModelNotFoundException();
                } else {
                    throw new ClientException();
                }
            }

            @Override
            public void deleteGroupImage(String filename) {
                if (status != 404) {
                    throw new ClientException();
                }
            }
        };
    }
}
