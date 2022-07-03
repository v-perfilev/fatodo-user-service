package com.persoff68.fatodo.config;

import com.persoff68.fatodo.client.ImageServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final BeanFactory beanFactory;

    @Bean
    @Primary
    public ImageServiceClient imageClient() {
        return (ImageServiceClient) beanFactory.getBean("imageServiceClientWrapper");
    }


}
