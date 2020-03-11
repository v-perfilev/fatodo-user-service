package com.persoff68.fatodo;

import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;

import java.util.Collections;

public class FactoryUtils {

    public static User createUser_local(String id) {
        User user = new User();
        user.setId("test_id_" + id);
        user.setEmail("test_" + id + "@email.com");
        user.setUsername("test_username_" + id);
        user.setPassword("$2a$10$GZrq9GxkRWW1Pv7fKJHGAe4ebib6113zhlU4nZlCtH/ylebR9rkn6");
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setProvider("LOCAL");
        return user;
    }

    public static User createUser_oAuth2(String id, String provider) {
        User user = new User();
        user.setId("test_id_" + id);
        user.setEmail("test_" + id + "@email.com");
        user.setUsername("test_" + id + "@email.com");
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setProvider(provider);
        user.setProviderId("test_id_" + id);
        return user;
    }

    public static LocalUserDTO createLocalUserDTO(String id) {
        LocalUserDTO dto = new LocalUserDTO();
        dto.setEmail("test_" + id + "@email.com");
        dto.setUsername("test_username_" + id);
        dto.setPassword("test_password");
        return dto;
    }

    public static LocalUserDTO createInvalidLocalUserDTO() {
        return new LocalUserDTO();
    }

    public static OAuth2UserDTO createOAuth2UserDTO(String id, String provider) {
        OAuth2UserDTO dto = new OAuth2UserDTO();
        dto.setEmail("test_" + id + "@email.com");
        dto.setUsername("test_username_" + id);
        dto.setProvider(provider);
        dto.setProviderId("test_providerId_" + id);
        return dto;
    }

    public static OAuth2UserDTO createInvalidOAuth2UserDTO() {
        return new OAuth2UserDTO();
    }

}
