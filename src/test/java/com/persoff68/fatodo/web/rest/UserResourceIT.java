package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FaToDoUserServiceApplication;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FaToDoUserServiceApplication.class)
public class UserResourceIT {
    private static final String ENDPOINT = "/users";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        userRepository.deleteAll();
        for (int i = 1; i <= 5; i++) {
            userRepository.save(createUser(i));
        }
    }

    @Test
    void testGetAll_correct() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType userDTOListClass = objectMapper.getTypeFactory().constructCollectionType(List.class, UserDTO.class);
        List<UserDTO> userDTOList = objectMapper.readValue(resultString, userDTOListClass);

        assertThat(userDTOList).hasSize(5);
    }

    @Test
    void testGetById_correct() throws Exception {
        UserDTO userDTO = createUserDTO(1);
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/test_id_1"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO result = objectMapper.readValue(resultString, UserDTO.class);

        assertThat(result).isEqualTo(userDTO);
    }

    @Test
    void testGetById_notFound() throws Exception {
        mvc.perform(get(ENDPOINT + "/test_id_0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserPrincipalByUsername_notFound() throws Exception {
        mvc.perform(get(ENDPOINT + "/username/test_username_0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate_ifNotExists() throws Exception {
        UserDTO userDTO = createUserDTOWithoutId(6);
        String json = objectMapper.writeValueAsString(userDTO);

        ResultActions resultActions = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO result = objectMapper.readValue(resultString, UserDTO.class);

        assertThat(result.getId()).isNotNull();
    }

    @Test
    void testCreate_ifExistsWithId() throws Exception {
        UserDTO userDTO = createUserDTO(1);
        String json = objectMapper.writeValueAsString(userDTO);

        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreate_ifExistsWithoutId() throws Exception {
        UserDTO userDTO = createUserDTOWithoutId(1);
        String json = objectMapper.writeValueAsString(userDTO);

        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }



    @Test
    void testUpdate_ifNotExists() throws Exception {
        UserDTO userDTO = createUserDTO(6);
        String json = objectMapper.writeValueAsString(userDTO);

        mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_ifExists() throws Exception {
        UserDTO userDTO = createUserDTO(1);
        userDTO.setUsername("new_name");
        String json = objectMapper.writeValueAsString(userDTO);


        ResultActions resultActions = mvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO result = objectMapper.readValue(resultString, UserDTO.class);

        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getUsername()).isEqualTo("new_name");
    }

    @Test
    void testDelete_ifNotExists() throws Exception {
        mvc.perform(delete(ENDPOINT + "/test_id_6"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_ifExists() throws Exception {
        mvc.perform(delete(ENDPOINT + "/test_id_1"))
                .andExpect(status().isOk());
    }


    private static User createUser(int id) {
        User user = new User();
        user.setId("test_id_" + id);
        user.setUsername("test_username_" + id);
        user.setEmail("test_" + id + "@email.com");
        user.setPassword("test_password_" + id);
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setProvider("test_provider_" + id);
        return user;
    }

    private static UserDTO createUserDTO(int id) {
        UserDTO dto = new UserDTO();
        dto.setId("test_id_" + id);
        dto.setUsername("test_username_" + id);
        dto.setEmail("test_" + id + "@email.com");
        dto.setAuthorities(Collections.singleton("ROLE_USER"));
        dto.setProvider("test_provider_" + id);
        return dto;
    }

    private static UserDTO createUserDTOWithoutId(int id) {
        UserDTO dto = new UserDTO();
        dto.setUsername("test_username_" + id);
        dto.setEmail("test_" + id + "@email.com");
        dto.setAuthorities(Collections.singleton("ROLE_USER"));
        dto.setProvider("test_provider_" + id);
        return dto;
    }

}
