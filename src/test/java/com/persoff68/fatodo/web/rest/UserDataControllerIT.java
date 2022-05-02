package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestUser;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserInfoDTO;
import com.persoff68.fatodo.model.dto.UserSummaryDTO;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
class UserDataControllerIT {
    private static final String ENDPOINT = "/api/user-data";

    private static final UUID CURRENT_ID = UUID.fromString("6e3c489b-a4fb-4654-aa39-30985b7c4656");
    private static final String CURRENT_NAME = "current-name";
    private static final String LOCAL_NAME = "local-name";

    @Autowired
    WebApplicationContext context;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        User currentUser = TestUser.defaultBuilder()
                .id(CURRENT_ID)
                .username(CURRENT_NAME)
                .email(CURRENT_NAME + "@email.com")
                .activated(false)
                .build();

        User localUser = TestUser.defaultBuilder()
                .username(LOCAL_NAME)
                .email(LOCAL_NAME + "@email.com")
                .build();

        userRepository.deleteAll();
        userRepository.save(currentUser);
        userRepository.save(localUser);
    }

    @Test
    @WithCustomSecurityContext
    void testGetAllByIds_ok() throws Exception {
        String url = ENDPOINT + "/all/ids";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserSummaryDTO.class);
        List<UserSummaryDTO> userSummaryDTOList = objectMapper.readValue(resultString, collectionType);
        assertThat(userSummaryDTOList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetAllByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "/all/ids";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext
    void testGetInfoByIds_ok() throws Exception {
        String url = ENDPOINT + "/info/ids";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserSummaryDTO.class);
        List<UserInfoDTO> userSummaryDTOList = objectMapper.readValue(resultString, collectionType);
        assertThat(userSummaryDTOList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetInfoByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "/info/ids";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext
    void testGetAllByUsername_ok() throws Exception {
        String usernamePart = CURRENT_NAME.substring(0, 4).toUpperCase();
        String url = ENDPOINT + "/all/" + usernamePart + "/username-part";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserSummaryDTO.class);
        List<UserSummaryDTO> dtoList = objectMapper.readValue(resultString, collectionType);
        assertThat(dtoList).hasSize(1);
        assertThat(dtoList.get(0).getUsername()).isEqualTo(CURRENT_NAME);
    }

    @Test
    @WithCustomSecurityContext
    void testGetAllByUsername_ok_empty() throws Exception {
        String usernamePart = CURRENT_NAME.substring(1, 4);
        String url = ENDPOINT + "/all/" + usernamePart + "/username-part";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserSummaryDTO.class);
        List<UserSummaryDTO> dtoList = objectMapper.readValue(resultString, collectionType);
        assertThat(dtoList).isEmpty();
    }

    @Test
    @WithAnonymousUser
    void testGetAllByUsername_unauthorized() throws Exception {
        String usernamePart = "current";
        String url = ENDPOINT + "/all/" + usernamePart + "/username-part";
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

    @Test
    @WithCustomSecurityContext
    void testGetAllUsernamesByIds_ok() throws Exception {
        String url = ENDPOINT + "/usernames/ids";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        List<String> usernameList = objectMapper.readValue(resultString, collectionType);
        assertThat(usernameList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetAllUsernamesByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "/usernames/ids";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetByUsernameOrEmail_ok_username() throws Exception {
        String username = LOCAL_NAME;
        String url = ENDPOINT + "/" + username + "/username-or-email";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserSummaryDTO resultDTO = objectMapper.readValue(resultString, UserSummaryDTO.class);
        assertThat(resultDTO.getUsername()).isEqualTo(username);
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetByUsernameOrEmail_ok_email() throws Exception {
        String email = LOCAL_NAME + "@email.com";
        String url = ENDPOINT + "/" + email + "/username-or-email";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserSummaryDTO resultDTO = objectMapper.readValue(resultString, UserSummaryDTO.class);
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
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetByUsernameOrEmail_notFound() throws Exception {
        String username = "not_exists";
        String url = ENDPOINT + "/" + username + "/username-or-email";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetByUsername_ok() throws Exception {
        String username = LOCAL_NAME;
        String url = ENDPOINT + "/" + username + "/username";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserSummaryDTO resultDTO = objectMapper.readValue(resultString, UserSummaryDTO.class);
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
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetByUsername_notFound() throws Exception {
        String username = "not_exists";
        String url = ENDPOINT + "/" + username + "/username";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

}
