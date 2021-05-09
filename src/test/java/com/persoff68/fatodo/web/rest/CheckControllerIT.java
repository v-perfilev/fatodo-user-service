package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.builder.TestUser;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
public class CheckControllerIT {
    private static final String ENDPOINT = "/api/check";

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String LOCAL_NAME = "local-name";
    private static final String NOT_EXISTING_NAME = "not-existing-name";

    @Autowired
    WebApplicationContext context;
    @Autowired
    UserRepository userRepository;

    MockMvc mvc;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        User localUser = TestUser.defaultBuilder()
                .id(USER_ID)
                .username(LOCAL_NAME)
                .email(LOCAL_NAME + "@email.com")
                .build();

        userRepository.deleteAll();
        userRepository.save(localUser);
    }


    @Test
    @WithAnonymousUser
    public void testDoesEmailExist_false() throws Exception {
        String email = NOT_EXISTING_NAME + "@email.com";
        String url = ENDPOINT + "/email/" + email;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isFalse();
    }

    @Test
    @WithAnonymousUser
    public void testDoesEmailExist_true() throws Exception {
        String email = LOCAL_NAME + "@email.com";
        String url = ENDPOINT + "/email/" + email;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isTrue();
    }

    @Test
    @WithAnonymousUser
    public void testDoesUsernameExist_false() throws Exception {
        String url = ENDPOINT + "/username/" + NOT_EXISTING_NAME;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isFalse();
    }

    @Test
    @WithAnonymousUser
    public void testDoesUsernameExist_true() throws Exception {
        String url = ENDPOINT + "/username/" + LOCAL_NAME;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isTrue();
    }

    @Test
    @WithAnonymousUser
    public void testDoesUsernameOrEmailExist_false() throws Exception {
        String url = ENDPOINT + "/username-or-email/" + NOT_EXISTING_NAME;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isFalse();
    }

    @Test
    @WithAnonymousUser
    public void testDoesUsernameOrEmailExist_true_email() throws Exception {
        String email = LOCAL_NAME + "@email.com";
        String url = ENDPOINT + "/username-or-email/" + email;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isTrue();
    }

    @Test
    @WithAnonymousUser
    public void testDoesUsernameOrEmailExist_true_username() throws Exception {
        String url = ENDPOINT + "/username-or-email/" + LOCAL_NAME;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isTrue();
    }

    @Test
    @WithAnonymousUser
    public void testDoesIdExist_false() throws Exception {
        String url = ENDPOINT + "/id/" + UUID.randomUUID();
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isFalse();
    }

    @Test
    @WithAnonymousUser
    public void testDoesIdExist_true() throws Exception {
        String url = ENDPOINT + "/id/" + USER_ID;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean doesExist = Boolean.parseBoolean(resultString);
        assertThat(doesExist).isTrue();
    }

}
