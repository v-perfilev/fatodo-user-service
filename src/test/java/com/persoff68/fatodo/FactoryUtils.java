package com.persoff68.fatodo;

import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.UserManagementDTO;

import java.util.Collections;

public class FactoryUtils {

    public static User createUser_local(String postfix, String password) {
        User user = new User();
        user.setId("test_id_" + postfix);
        user.setEmail("test_" + postfix + "@email.com");
        user.setUsername("test_username_" + postfix);
        user.setPassword(password);
        user.setAuthorities(Collections.singleton(new Authority(AuthorityType.USER.getValue())));
        user.setProvider(Provider.LOCAL);
        user.setLanguage("en");
        return user;
    }

    public static User createUser_oAuth2(String postfix, String provider) {
        User user = new User();
        user.setId("test_id_" + postfix);
        user.setEmail("test_" + postfix + "@email.com");
        user.setUsername("test_" + postfix + "@email.com");
        user.setAuthorities(Collections.singleton(new Authority(AuthorityType.USER.getValue())));
        user.setProvider(Provider.valueOf(provider));
        user.setProviderId("test_id_" + postfix);
        user.setLanguage("en");
        return user;
    }

    public static LocalUserDTO createLocalUserDTO(String postfix) {
        LocalUserDTO dto = new LocalUserDTO();
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setPassword("test_password");
        dto.setLanguage("en");
        return dto;
    }

    public static LocalUserDTO createInvalidLocalUserDTO() {
        return new LocalUserDTO();
    }

    public static OAuth2UserDTO createOAuth2UserDTO(String postfix, String provider) {
        OAuth2UserDTO dto = new OAuth2UserDTO();
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setProvider(provider);
        dto.setProviderId("test_providerId_" + postfix);
        dto.setLanguage("en");
        return dto;
    }

    public static OAuth2UserDTO createInvalidOAuth2UserDTO() {
        return new OAuth2UserDTO();
    }

    public static UserManagementDTO createUserDTO_local(String postfix) {
        UserManagementDTO dto = new UserManagementDTO();
        dto.setId("test_id_" + postfix);
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setProvider(Provider.LOCAL.getValue());
        dto.setAuthorities(Collections.singleton(AuthorityType.USER.getValue()));
        dto.setLanguage("en");
        dto.setActivated(false);
        return dto;
    }

    public static UserManagementDTO createInvalidUserDTO_local() {
        return new UserManagementDTO();
    }

    public static ResetPasswordDTO createResetPasswordDTO(String userId) {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setUserId(userId);
        dto.setPassword("reset_password");
        return dto;
    }

}
