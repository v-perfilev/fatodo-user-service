package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserDTO;
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

import static com.persoff68.fatodo.FactoryUtils.createUser_local;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
public class UserControllerIT {
    private static final String ENDPOINT = "/api/user";

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
        User currentUser = FactoryUtils.createUser_local("current", "encodedPassword");
        currentUser.setId("3");
        userRepository.save(currentUser);
        userRepository.save(createUser_local("local", "encodedPassword"));
        userRepository.save(FactoryUtils.createUser_oAuth2("oauth2", Provider.GOOGLE.getValue()));
    }

    @Test
    @WithCustomSecurityContext(id = "3")
    public void testGetCurrentUser_ok() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isEqualTo("3");
    }

    @Test
    @WithAnonymousUser
    public void testGetCurrentUser_unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext
    public void testGetAllByIds_ok() throws Exception {
        String url = ENDPOINT + "/all-by-ids";
        String requestBody = objectMapper.writeValueAsString(List.of("3"));
        ResultActions resultActions = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserSummaryDTO.class);
        List<UserSummaryDTO> userSummaryDTOList = objectMapper.readValue(resultString, collectionType);
        assertThat(userSummaryDTOList.size()).isEqualTo(1);
    }

    @Test
    @WithAnonymousUser
    public void testGetAllByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "/all-by-ids";
        String requestBody = objectMapper.writeValueAsString(List.of("3"));
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

}
