package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FaToDoUserServiceApplication;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.config.constant.Authorities;
import com.persoff68.fatodo.config.constant.Providers;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
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

@SpringBootTest(classes = FaToDoUserServiceApplication.class)
public class UserResourceIT {
    private static final String ENDPOINT = "/users";

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
        userRepository.save(FactoryUtils.createUser_local("1"));
        userRepository.save(FactoryUtils.createUser_local("2"));
    }


    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
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
    @WithMockUser(authorities = Authorities.SYSTEM)
    public void testGetAll_forbidden() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testGetById_ok() throws Exception {
        String id = "test_id_1";
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
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = Authorities.SYSTEM)
    public void testGetById_forbidden() throws Exception {
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testGetById_notFound() throws Exception {
        String id = "test_id_not_exists";
        String url = ENDPOINT + "/" + id;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testCreate_created() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("not_exists");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getProvider()).isEqualTo(Providers.LOCAL);
        assertThat(resultDTO.getAuthorities()).containsOnly(Authorities.USER);
    }

    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testCreate_duplicated() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testCreate_invalid() throws Exception {
        UserDTO dto = FactoryUtils.createInvalidUserDTO_local();
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testCreate_unauthorized() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("not_exists");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = Authorities.SYSTEM)
    public void testCreate_forbidden() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("not_exists");
        dto.setId(null);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testUpdate_updated() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_updated@email.com");
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
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testUpdate_duplicated() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_2@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testUpdate_invalid() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testUpdate_unauthorized() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_updated@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = Authorities.SYSTEM)
    public void testUpdate_forbidden() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("1");
        dto.setEmail("test_updated@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testUpdate_notExists() throws Exception {
        UserDTO dto = FactoryUtils.createUserDTO_local("not_exists");
        dto.setEmail("test_updated@email.com");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
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
    @WithMockUser(authorities = Authorities.SYSTEM)
    public void testDelete_forbidden() throws Exception {
        String id = "test_id_1";
        String url = ENDPOINT + "/" + id;
        mvc.perform(delete(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = Authorities.ADMIN)
    public void testDelete_notFound() throws Exception {
        String id = "test_id_not_exists";
        String url = ENDPOINT + "/" + id;
        mvc.perform(delete(url))
                .andExpect(status().isNotFound());
    }

}
