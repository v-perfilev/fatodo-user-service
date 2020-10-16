package com.persoff68.fatodo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.web.rest.vm.UserVM;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Map;

public class FactoryUtils {

    public static User createUser_local(String postfix, String password) {
        User user = new User();
        user.setId("test_id_" + postfix);
        user.setEmail("test_" + postfix + "@email.com");
        user.setUsername("test_username_" + postfix);
        user.setImageFilename("test_image_filename");
        user.setLanguage("en");
        user.setAuthorities(Collections.singleton(new Authority(AuthorityType.USER.getValue())));
        user.setPassword(password);
        user.setProvider(Provider.LOCAL);
        return user;
    }

    public static User createUser_oAuth2(String postfix, String provider) {
        User user = new User();
        user.setId("test_id_" + postfix);
        user.setEmail("test_" + postfix + "@email.com");
        user.setUsername("test_" + postfix + "@email.com");
        user.setImageFilename("test_image_filename");
        user.setLanguage("en");
        user.setAuthorities(Collections.singleton(new Authority(AuthorityType.USER.getValue())));
        user.setProvider(Provider.valueOf(provider));
        user.setProviderId("test_id_" + postfix);
        return user;
    }

    public static LocalUserDTO createLocalUserDTO(String postfix) {
        LocalUserDTO dto = new LocalUserDTO();
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setLanguage("en");
        dto.setPassword("test_password");
        return dto;
    }

    public static OAuth2UserDTO createOAuth2UserDTO(String postfix, String provider) {
        OAuth2UserDTO dto = new OAuth2UserDTO();
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setLanguage("en");
        dto.setProvider(provider);
        dto.setProviderId("test_providerId_" + postfix);
        return dto;
    }

    public static UserDTO createUserDTO_local(String postfix) {
        UserDTO dto = new UserDTO();
        dto.setId("test_id_" + postfix);
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setLanguage("en");
        dto.setAuthorities(Collections.singleton(AuthorityType.USER.getValue()));
        dto.setProvider(Provider.LOCAL.getValue());
        dto.setActivated(false);
        return dto;
    }

    public static ResetPasswordDTO createResetPasswordDTO(String userId) {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setUserId(userId);
        dto.setPassword("reset_password");
        return dto;
    }

    public static UserVM createUserVM(String id, String postfix) {
        UserVM userVM = new UserVM();
        userVM.setId(id);
        userVM.setUsername("test_username_" + postfix);
        userVM.setLanguage("test_language_" + postfix);
        return userVM;
    }

    public static MultiValueMap<String, String> objectToMap(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(object);
        Map<String, String> map = objectMapper.readValue(json, Map.class);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        map.forEach(multiValueMap::add);
        return multiValueMap;
    }

}
