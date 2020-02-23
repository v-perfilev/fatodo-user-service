package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoUserServiceApplication;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = FaToDoUserServiceApplication.class)
public class DetailControllerIT {
    private static final String ENDPOINT = "/details";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    UserPrincipal testUserPrincipal;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        testUserPrincipal = createUserPrincipal(1);
        userRepository.save(createUser(1));
    }

    @Test
    void testGetUserPrincipalByUsername_correct() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/username/test_username_1"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipal result = objectMapper.readValue(resultString, UserPrincipal.class);

        assertThat(result).isEqualTo(testUserPrincipal);
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
        UserPrincipal result = objectMapper.readValue(resultString, UserPrincipal.class);

        assertThat(result).isEqualTo(testUserPrincipal);
    }

    @Test
    void testGetUserPrincipalByEmail_notFound() throws Exception {
        mvc.perform(get(ENDPOINT + "/email/test_0@email.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserPrincipalByEmailNullable_correct() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/email/test_1@email.com/nullable"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipal result = objectMapper.readValue(resultString, UserPrincipal.class);

        assertThat(result).isEqualTo(testUserPrincipal);
    }

    @Test
    void testGetUserPrincipalByEmailNullable_notFound() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/email/test_0@email.com/nullable"))
                .andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(resultString).isEqualTo("");
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


    private static UserPrincipal createUserPrincipal(int id) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId("test_id_" + id);
        userPrincipal.setUsername("test_username_" + id);
        userPrincipal.setEmail("test_" + id + "@email.com");
        userPrincipal.setPassword("test_password");
        userPrincipal.setAuthorities(Collections.singleton("ROLE_USER"));
        userPrincipal.setProvider("LOCAL");
        return userPrincipal;
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

}
