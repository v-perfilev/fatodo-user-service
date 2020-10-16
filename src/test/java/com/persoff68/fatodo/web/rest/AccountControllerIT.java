package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.web.rest.vm.UserVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static com.persoff68.fatodo.FactoryUtils.createUser_local;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
public class AccountControllerIT {
    private static final String ENDPOINT = "/api/account";

    @Autowired
    WebApplicationContext context;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ImageServiceClient imageServiceClient;

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

        when(imageServiceClient.createUserImage(any())).thenReturn("filename");
        when(imageServiceClient.updateUserImage(any())).thenReturn("filename");
        doNothing().when(imageServiceClient).deleteGroupImage(any());
    }

    @Test
    @WithCustomSecurityContext(id = "3")
    public void testGetCurrentUser_ok() throws Exception {
        String url = ENDPOINT + "/current";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isEqualTo("3");
    }

    @Test
    @WithAnonymousUser
    public void testGetCurrentUser_unauthorized() throws Exception {
        String url = ENDPOINT + "/current";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "3")
    public void testUpdate_ok() throws Exception {
        String url = ENDPOINT + "/update";
        UserVM vm = FactoryUtils.createUserVM("3", "updated");
        MultiValueMap<String, String> multiValueMap = FactoryUtils.objectToMap(vm);
        ResultActions resultActions = mvc.perform(post(url)
                .contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getUsername()).isEqualTo(vm.getUsername());
        assertThat(resultDTO.getLanguage()).isEqualTo(vm.getLanguage());
    }

    @Test
    @WithAnonymousUser
    public void testUpdate_unauthorized() throws Exception {
        String url = ENDPOINT + "/update";
        UserVM vm = FactoryUtils.createUserVM("3", "updated");
        MultiValueMap<String, String> multiValueMap = FactoryUtils.objectToMap(vm);
        mvc.perform(post(url)
                .contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "5")
    public void testUpdate_badRequest_wrongUser() throws Exception {
        String url = ENDPOINT + "/update";
        UserVM vm = FactoryUtils.createUserVM("3", "updated");
        MultiValueMap<String, String> multiValueMap = FactoryUtils.objectToMap(vm);
        mvc.perform(post(url)
                .contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomSecurityContext(id = "5")
    public void testUpdate_badRequest_notExists() throws Exception {
        String url = ENDPOINT + "/update";
        UserVM vm = FactoryUtils.createUserVM("5", "updated");
        MultiValueMap<String, String> multiValueMap = FactoryUtils.objectToMap(vm);
        mvc.perform(post(url)
                .contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap))
                .andExpect(status().isNotFound());
    }

}
