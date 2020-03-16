package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.FaToDoUserServiceApplication;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FaToDoUserServiceApplication.class)
public class CheckControllerIT {
    private static final String ENDPOINT = "/check";

    @Autowired
    WebApplicationContext context;
    @Autowired
    UserRepository userRepository;

    MockMvc mvc;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        userRepository.deleteAll();
        userRepository.save(FactoryUtils.createUser_local("local", "encodedPassword"));
    }


    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testIsEmailUnique_true() throws Exception {
        String email = "test_not_exists@email.com";
        String url = ENDPOINT + "/email/" + email + "/unique";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean isUnique = Boolean.parseBoolean(resultString);
        assertThat(isUnique).isTrue();
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testIsEmailUnique_false() throws Exception {
        String email = "test_local@email.com";
        String url = ENDPOINT + "/email/" + email + "/unique";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean isUnique = Boolean.parseBoolean(resultString);
        assertThat(isUnique).isFalse();
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testIsUsernameUnique_true() throws Exception {
        String username = "test_username_not_exists";
        String url = ENDPOINT + "/username/" + username + "/unique";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean isUnique = Boolean.parseBoolean(resultString);
        assertThat(isUnique).isTrue();
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testIsUsernameUnique_false() throws Exception {
        String username = "test_username_local";
        String url = ENDPOINT + "/username/" + username + "/unique";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean isUnique = Boolean.parseBoolean(resultString);
        assertThat(isUnique).isFalse();
    }

}
