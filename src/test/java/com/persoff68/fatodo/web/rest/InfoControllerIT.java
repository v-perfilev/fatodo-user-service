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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void testGetAllUserSummaryByIds_ok() throws Exception {
        String url = ENDPOINT + "/summary";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                UserSummaryDTO.class);
        List<UserSummaryDTO> userSummaryDTOList = objectMapper.readValue(resultString, collectionType);
        assertThat(userSummaryDTOList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetAllUserSummaryByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "/summary";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext
    void testGetAllUserInfoByIds_ok() throws Exception {
        String url = ENDPOINT + "/info";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                UserSummaryDTO.class);
        List<UserInfoDTO> userSummaryDTOList = objectMapper.readValue(resultString, collectionType);
        assertThat(userSummaryDTOList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetAllUserInfoByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "/info";
        String requestBody = objectMapper.writeValueAsString(List.of(CURRENT_ID));
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }


}
