package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestUser;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserInfoDTO;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
@AutoConfigureMockMvc
class InfoControllerIT {
    private static final String ENDPOINT = "/api/info";

    private static final UUID CURRENT_ID = UUID.fromString("6e3c489b-a4fb-4654-aa39-30985b7c4656");
    private static final String CURRENT_NAME = "current-name";
    private static final String LOCAL_NAME = "local-name";

    @Autowired
    MockMvc mvc;

    @Autowired
    WebApplicationContext context;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        User currentUser = TestUser.defaultBuilder()
                .id(CURRENT_ID)
                .username(CURRENT_NAME)
                .email(CURRENT_NAME + "@email.com")
                .activated(false)
                .deleted(true)
                .build();

        User localUser = TestUser.defaultBuilder()
                .username(LOCAL_NAME)
                .email(LOCAL_NAME + "@email.com")
                .build();

        userRepository.save(currentUser);
        userRepository.save(localUser);
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @WithCustomSecurityContext
    void testGetAllByIds_ok() throws Exception {
        String url = ENDPOINT + "?ids=" + CURRENT_ID;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                UserInfoDTO.class);
        List<UserInfoDTO> userInfoDTOList = objectMapper.readValue(resultString, collectionType);
        assertThat(userInfoDTOList).hasSize(1);
        assertThat(userInfoDTOList.get(0).isDeleted()).isTrue();
    }

    @Test
    @WithAnonymousUser
    void testGetAllByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "?ids=" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext
    void testGetAllByUsernamePart_ok() throws Exception {
        String usernamePart = CURRENT_NAME.substring(0, 4).toUpperCase();
        String url = ENDPOINT + "/" + usernamePart + "/username-part";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                UserInfoDTO.class);
        List<UserInfoDTO> dtoList = objectMapper.readValue(resultString, collectionType);
        assertThat(dtoList).hasSize(1);
        assertThat(dtoList.get(0).getUsername()).isEqualTo(CURRENT_NAME);
    }

    @Test
    @WithCustomSecurityContext
    void testGetAllByUsernamePart_ok_empty() throws Exception {
        String usernamePart = CURRENT_NAME.substring(1, 4);
        String url = ENDPOINT + "/" + usernamePart + "/username-part";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                UserInfoDTO.class);
        List<UserInfoDTO> dtoList = objectMapper.readValue(resultString, collectionType);
        assertThat(dtoList).isEmpty();
    }

    @Test
    @WithAnonymousUser
    void testGetAllByUsernamePart_unauthorized() throws Exception {
        String usernamePart = "current";
        String url = ENDPOINT + "/" + usernamePart + "/username-part";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext
    void testGetByUsername_ok() throws Exception {
        String username = LOCAL_NAME;
        String url = ENDPOINT + "/" + username + "/username";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserInfoDTO resultDTO = objectMapper.readValue(resultString, UserInfoDTO.class);
        assertThat(resultDTO.getUsername()).isEqualTo(username);
    }

    @Test
    @WithAnonymousUser
    void testGetByUsername_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + LOCAL_NAME + "/username";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext
    void testGetByUsername_notFound() throws Exception {
        String username = "not_exists";
        String url = ENDPOINT + "/" + username + "/username";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext
    void testGetByUsernameOrEmail_ok_username() throws Exception {
        String username = LOCAL_NAME;
        String url = ENDPOINT + "/" + username + "/username-or-email";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserInfoDTO resultDTO = objectMapper.readValue(resultString, UserInfoDTO.class);
        assertThat(resultDTO.getUsername()).isEqualTo(username);
    }

    @Test
    @WithCustomSecurityContext
    void testGetByUsernameOrEmail_ok_email() throws Exception {
        String email = LOCAL_NAME + "@email.com";
        String url = ENDPOINT + "/" + email + "/username-or-email";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserInfoDTO resultDTO = objectMapper.readValue(resultString, UserInfoDTO.class);
        assertThat(resultDTO).isNotNull();
    }

    @Test
    @WithAnonymousUser
    void testGetByUsernameOrEmail_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + LOCAL_NAME + "/username-or-email";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext
    void testGetByUsernameOrEmail_notFound() throws Exception {
        String username = "not_exists";
        String url = ENDPOINT + "/" + username + "/username-or-email";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext
    void testGetAllUsernamesByIds_ok() throws Exception {
        String url = ENDPOINT + "/username?ids=" + CURRENT_ID;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        List<String> usernameList = objectMapper.readValue(resultString, collectionType);
        assertThat(usernameList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetAllUsernamesByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "/username?ids=" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext
    void testGetAllIdsByUsername_ok() throws Exception {
        String usernamePart = CURRENT_NAME.substring(0, 4).toUpperCase();
        String url = ENDPOINT + "/ids/" + usernamePart + "/username-part";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, UUID.class);
        List<UUID> idsList = objectMapper.readValue(resultString, collectionType);
        assertThat(idsList).hasSize(1);
        assertThat(idsList.get(0)).isNotNull();
    }

    @Test
    @WithCustomSecurityContext
    void testGetAllIdsByUsername_ok_empty() throws Exception {
        String usernamePart = CURRENT_NAME.substring(1, 4);
        String url = ENDPOINT + "/ids/" + usernamePart + "/username-part";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, UUID.class);
        List<UUID> idsList = objectMapper.readValue(resultString, collectionType);
        assertThat(idsList).isEmpty();
    }

    @Test
    @WithAnonymousUser
    void testGetAllIdsByUsername_unauthorized() throws Exception {
        String usernamePart = "current";
        String url = ENDPOINT + "/ids/" + usernamePart + "/username-part";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

}
