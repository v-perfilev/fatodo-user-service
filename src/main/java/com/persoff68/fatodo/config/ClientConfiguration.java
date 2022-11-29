package com.persoff68.fatodo.config;

import com.persoff68.fatodo.client.ChatSystemServiceClient;
import com.persoff68.fatodo.client.ContactSystemServiceClient;
import com.persoff68.fatodo.client.EventSystemServiceClient;
import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.client.ItemSystemServiceClient;
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
    public ChatSystemServiceClient chatClient() {
        return (ChatSystemServiceClient) beanFactory.getBean("chatSystemServiceClientWrapper");
    }

    @Bean
    @Primary
    public ContactSystemServiceClient contactClient() {
        return (ContactSystemServiceClient) beanFactory.getBean("contactSystemServiceClientWrapper");
    }

    @Bean
    @Primary
    public EventSystemServiceClient eventClient() {
        return (EventSystemServiceClient) beanFactory.getBean("eventSystemServiceClientWrapper");
    }

    @Bean
    @Primary
    public ImageServiceClient imageClient() {
        return (ImageServiceClient) beanFactory.getBean("imageServiceClientWrapper");
    }

    @Bean
    @Primary
    public ItemSystemServiceClient itemClient() {
        return (ItemSystemServiceClient) beanFactory.getBean("itemSystemServiceClientWrapper");
    }

}
