package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestUser;
import com.persoff68.fatodo.builder.TestUserDTO;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
public class UserResourceIT {
    private static final String ENDPOINT = "/api/users";

    private static final UUID CURRENT_ID = UUID.fromString("6e3c489b-a4fb-4654-aa39-30985b7c4656");
    private static final String CURRENT_NAME = "current-name";

    @Autowired
    WebApplicationContext context;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc mvc;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        User currentUser = TestUser.defaultBuilder()
                .id(CURRENT_ID)
                .username(CURRENT_NAME)
                .email(CURRENT_NAME + "@email.com")
                .build();

        User anotherUser = TestUser.defaultBuilder().build();

        userRepository.deleteAll();
        userRepository.save(currentUser);
        userRepository.save(anotherUser);
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testGetAll_ok() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class);
        List<UserDTO> resultDTOList = objectMapper.readValue(resultString, listType);
        assertThat(resultDTOList.size()).isEqualTo(2);
    }

    @Test
    @WithAnonymousUser
    public void testGetAll_unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetAll_forbidden() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testGetById_ok() throws Exception {
        UUID id = CURRENT_ID;
        String url = ENDPOINT + "/" + id;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isEqualTo(id);
    }

    @Test
    @WithAnonymousUser
    public void testGetById_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetById_forbidden() throws Exception {
        String url = ENDPOINT + "/" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testGetById_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        String url = ENDPOINT + "/" + id;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testCreate_created() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder().id(null).username("new-name").email("new-name@email.com").build();
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getInfo()).isEqualTo(dto.getInfo());
        assertThat(resultDTO.getProvider()).isEqualTo(Provider.LOCAL.getValue());
        assertThat(resultDTO.getAuthorities()).containsOnly(AuthorityType.USER.getValue());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testCreate_conflict_duplicated() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder().id(null).username(CURRENT_NAME).build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testCreate_badRequest_invalid() throws Exception {
        UserDTO dto = new UserDTO();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testCreate_unauthorized() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder().id(null).username("new-name").email("new-name@email.com").build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testCreate_forbidden() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder().id(null).username("new-name").email("new-name@email.com").build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_ok() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder()
                .id(CURRENT_ID)
                .username("updated-name")
                .email("updated-name@email.com")
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isEqualTo(dto.getId());
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getProvider()).isEqualTo(dto.getProvider());
        assertThat(resultDTO.getAuthorities()).isEqualTo(dto.getAuthorities());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_conflict_duplicated() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder()
                .id(CURRENT_ID)
                .username(CURRENT_NAME)
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_badRequest_invalidEmail() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder()
                .id(CURRENT_ID)
                .username("updated-name")
                .email("updated-name")
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_badRequest_noId() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder()
                .id(null)
                .username("updated-name")
                .email("updated-name@email.com")
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testUpdate_unauthorized() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder()
                .id(CURRENT_ID)
                .username("updated-name")
                .email("updated-name@email.com")
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    public void testUpdate_forbidden() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder()
                .id(CURRENT_ID)
                .username("updated-name")
                .email("updated-name@email.com")
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_notFound() throws Exception {
        UserDTO dto = TestUserDTO.defaultBuilder()
                .id(UUID.randomUUID())
                .username("updated-name")
                .email("updated-name@email.com")
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testDelete_ok() throws Exception {
        String url = ENDPOINT + "/" + CURRENT_ID;
        mvc.perform(delete(url))
                .andExpect(status().isOk());
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    public void testDelete_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + CURRENT_ID;
        mvc.perform(delete(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testDelete_forbidden() throws Exception {
        String url = ENDPOINT + "/" + CURRENT_ID;
        mvc.perform(delete(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testDelete_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        String url = ENDPOINT + "/" + id;
        mvc.perform(delete(url))
                .andExpect(status().isNotFound());
    }

}
