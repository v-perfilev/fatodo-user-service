package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoUserServiceApplication;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = FaToDoUserServiceApplication.class)
public class AuthControllerIT {
    private static final String ENDPOINT = "/auth";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    UserPrincipalDTO testUserPrincipalDTO;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
        testUserPrincipalDTO = createUserPrincipal(1);
        userRepository.save(createUser(1));
    }

    @Test
    void testGetUserPrincipalByUsername_correct() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/username/test_username_1"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO result = objectMapper.readValue(resultString, UserPrincipalDTO.class);

        assertThat(result).isEqualTo(testUserPrincipalDTO);
    }

    @Test
    void testGetUserPrincipalByUsername_notFound() throws Exception {
        mvc.perform(get(ENDPOINT + "/username/test_username_0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserPrincipalByEmail_correct() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/email/test_1@email.com"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO result = objectMapper.readValue(resultString, UserPrincipalDTO.class);

        assertThat(result).isEqualTo(testUserPrincipalDTO);
    }

    @Test
    void testGetUserPrincipalByEmail_notFound() throws Exception {
        mvc.perform(get(ENDPOINT + "/email/test_0@email.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testIsUsernameUnique_true() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/username/test_username_6/unique"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean isUnique = Boolean.parseBoolean(resultString);
        assertThat(isUnique).isEqualTo(true);
    }

    @Test
    void testIsUsernameUnique_false() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/username/test_username_1/unique"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean isUnique = Boolean.parseBoolean(resultString);
        assertThat(isUnique).isEqualTo(false);
    }

    @Test
    void testIsEmailUnique_true() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/email/test_6@email.com/unique"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean isUnique = Boolean.parseBoolean(resultString);
        assertThat(isUnique).isEqualTo(true);
    }

    @Test
    void testIsEmailUnique_false() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/email/test_1@email.com/unique"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean isUnique = Boolean.parseBoolean(resultString);
        assertThat(isUnique).isEqualTo(false);
    }

    @Test
    void testCreateOAuth2_ifNotExists() throws Exception {
        OAuth2UserDTO oAuth2UserDTO = createOAuth2UserDTOWithoutId(6);
        String json = objectMapper.writeValueAsString(oAuth2UserDTO);

        ResultActions resultActions = mvc.perform(post(ENDPOINT + "/create-oauth2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO result = objectMapper.readValue(resultString, UserDTO.class);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getProvider()).isEqualTo("GOOGLE");
    }

    @Test
    void testCreateOAuth2_ifExistsWithoutId() throws Exception {
        OAuth2UserDTO oAuth2UserDTO = createOAuth2UserDTOWithoutId(1);
        String json = objectMapper.writeValueAsString(oAuth2UserDTO);

        mvc.perform(post(ENDPOINT + "/create-oauth2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateOAuth2_ifExistsWithId() throws Exception {
        OAuth2UserDTO oAuth2UserDTO = createOAuth2UserDTO(1);
        String json = objectMapper.writeValueAsString(oAuth2UserDTO);

        mvc.perform(post(ENDPOINT + "/create-oauth2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateLocal_ifNotExists() throws Exception {
        LocalUserDTO localUserDTO = createLocalUserDTOWithoutId(6);
        String json = objectMapper.writeValueAsString(localUserDTO);

        ResultActions resultActions = mvc.perform(post(ENDPOINT + "/create-local")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO result = objectMapper.readValue(resultString, UserDTO.class);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getProvider()).isEqualTo("LOCAL");
    }

    @Test
    void testCreateLocal_ifExistsWithId() throws Exception {
        LocalUserDTO localUserDTO = createLocalUserDTO(1);
        String json = objectMapper.writeValueAsString(localUserDTO);

        mvc.perform(post(ENDPOINT + "/create-local")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateLocal_ifExistsWithoutId() throws Exception {
        LocalUserDTO localUserDTO = createLocalUserDTOWithoutId(1);
        String json = objectMapper.writeValueAsString(localUserDTO);

        mvc.perform(post(ENDPOINT + "/create-local")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }


    private static UserPrincipalDTO createUserPrincipal(int id) {
        UserPrincipalDTO userPrincipalDTO = new UserPrincipalDTO();
        userPrincipalDTO.setId("test_id_" + id);
        userPrincipalDTO.setUsername("test_username_" + id);
        userPrincipalDTO.setEmail("test_" + id + "@email.com");
        userPrincipalDTO.setPassword("test_password");
        userPrincipalDTO.setAuthorities(Collections.singleton("ROLE_USER"));
        userPrincipalDTO.setProvider("LOCAL");
        return userPrincipalDTO;
    }

    private static User createUser(int id) {
        User user = new User();
        user.setId("test_id_" + id);
        user.setUsername("test_username_" + id);
        user.setEmail("test_" + id + "@email.com");
        user.setPassword("test_password");
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setProvider("LOCAL");
        return user;
    }


    private static OAuth2UserDTO createOAuth2UserDTO(int id) {
        OAuth2UserDTO dto = new OAuth2UserDTO();
        dto.setId("test_id_" + id);
        dto.setUsername("test_username_" + id);
        dto.setEmail("test_" + id + "@email.com");
        dto.setProvider("GOOGLE");
        dto.setProviderId("test_provider_id");
        return dto;
    }

    private static OAuth2UserDTO createOAuth2UserDTOWithoutId(int id) {
        OAuth2UserDTO dto = new OAuth2UserDTO();
        dto.setUsername("test_username_" + id);
        dto.setEmail("test_" + id + "@email.com");
        dto.setProvider("GOOGLE");
        dto.setProviderId("test_provider_id");
        return dto;
    }

    private static LocalUserDTO createLocalUserDTO(int id) {
        LocalUserDTO dto = new LocalUserDTO();
        dto.setId("test_id_" + id);
        dto.setUsername("test_username_" + id);
        dto.setEmail("test_" + id + "@email.com");
        dto.setPassword("test_password_" + id);
        dto.setProvider("LOCAL");
        return dto;
    }

    private static LocalUserDTO createLocalUserDTOWithoutId(int id) {
        LocalUserDTO dto = new LocalUserDTO();
        dto.setUsername("test_username_" + id);
        dto.setEmail("test_" + id + "@email.com");
        dto.setPassword("test_password_" + id);
        dto.setProvider("LOCAL");
        return dto;
    }

}
