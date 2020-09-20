package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.dto.UserManagementDTO;
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
        userRepository.deleteAll();
        userRepository.save(FactoryUtils.createUser_local("1", "encoded_password"));
        userRepository.save(FactoryUtils.createUser_local("2", "encoded_password"));
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testGetAll_ok() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserManagementDTO.class);
        List<UserManagementDTO> resultDTOList = objectMapper.readValue(resultString, listType);
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
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserManagementDTO resultDTO = objectMapper.readValue(resultString, UserManagementDTO.class);
        assertThat(resultDTO.getId()).isEqualTo(id);
    }

    @Test
    @WithAnonymousUser
    public void testGetById_unauthorized() throws Exception {
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetById_forbidden() throws Exception {
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testGetById_notFound() throws Exception {
        String id = "test_id_not_exists";
        String url = ENDPOINT + "/" + id;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testCreate_created() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("not_exists");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserManagementDTO resultDTO = objectMapper.readValue(resultString, UserManagementDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getProvider()).isEqualTo(Provider.LOCAL.getValue());
        assertThat(resultDTO.getAuthorities()).containsOnly(AuthorityType.USER.getValue());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testCreate_conflict_duplicated() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testCreate_badRequest_invalid() throws Exception {
        UserManagementDTO dto = FactoryUtils.createInvalidUserDTO_local();
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testCreate_unauthorized() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("not_exists");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testCreate_forbidden() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("not_exists");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_ok() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_updated@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserManagementDTO resultDTO = objectMapper.readValue(resultString, UserManagementDTO.class);
        assertThat(resultDTO.getId()).isEqualTo(dto.getId());
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getProvider()).isEqualTo(dto.getProvider());
        assertThat(resultDTO.getAuthorities()).isEqualTo(dto.getAuthorities());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_conflict_duplicated() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_2@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_badRequest_invalidEmail() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_badRequest_noId() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_2@email.com");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testUpdate_unauthorized() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_updated@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testUpdate_forbidden() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_updated@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testUpdate_notFound() throws Exception {
        UserManagementDTO dto = FactoryUtils.createUserDTO_local("not_exists");
        dto.setEmail("test_updated@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testDelete_ok() throws Exception {
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        mvc.perform(delete(url))
                .andExpect(status().isOk());
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    public void testDelete_unauthorized() throws Exception {
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        mvc.perform(delete(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testDelete_forbidden() throws Exception {
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        mvc.perform(delete(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_ADMIN")
    public void testDelete_notFound() throws Exception {
        String id = "test_id_not_exists";
        String url = ENDPOINT + "/" + id;
        mvc.perform(delete(url))
                .andExpect(status().isNotFound());
    }

}
