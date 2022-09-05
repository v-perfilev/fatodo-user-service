package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.TestUtils;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestChangePasswordVM;
import com.persoff68.fatodo.builder.TestUser;
import com.persoff68.fatodo.builder.TestUserVM;
import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.config.constant.Language;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.Info;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.vm.ChangeLanguageVM;
import com.persoff68.fatodo.model.vm.ChangePasswordVM;
import com.persoff68.fatodo.model.vm.UserVM;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
@AutoConfigureMockMvc
class AccountControllerIT {
    private static final String ENDPOINT = "/api/account";

    private static final UUID CURRENT_ID = UUID.fromString("6e3c489b-a4fb-4654-aa39-30985b7c4656");
    private static final UUID GOOGLE_ID = UUID.fromString("71afdeec-cffd-479f-90ca-12fca4167cda");
    private static final String CURRENT_NAME = "current-name";
    private static final String LOCAL_NAME = "local-name";
    private static final String GOOGLE_NAME = "google-name";

    @Autowired
    MockMvc mvc;

    @Autowired
    WebApplicationContext context;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    ImageServiceClient imageServiceClient;

    @BeforeEach
    void setup() {
        User currentUser = TestUser.defaultBuilder()
                .id(CURRENT_ID)
                .username(CURRENT_NAME)
                .email(CURRENT_NAME + "@email.com")
                .password(passwordEncoder.encode("test_password"))
                .build();

        User localUser = TestUser.defaultBuilder()
                .username(LOCAL_NAME)
                .email(LOCAL_NAME + "@email.com")
                .password(passwordEncoder.encode("test_password"))
                .build();

        User googleUser = TestUser.defaultBuilder()
                .id(GOOGLE_ID)
                .username(GOOGLE_NAME)
                .email(GOOGLE_NAME + "@email.com")
                .provider(Provider.GOOGLE)
                .build();

        userRepository.save(currentUser);
        userRepository.save(localUser);
        userRepository.save(googleUser);

        when(imageServiceClient.createUserImage(any())).thenReturn("filename");
        when(imageServiceClient.updateUserImage(any())).thenReturn("filename");
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testGetCurrentUser_ok() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isEqualTo(CURRENT_ID);
    }

    @Test
    @WithAnonymousUser
    void testGetCurrentUser_unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testUpdate_ok() throws Exception {
        UserVM vm = TestUserVM.defaultBuilder().id(CURRENT_ID).build();
        MultiValueMap<String, String> multiValueMap = TestUtils.objectToMap(vm);
        ResultActions resultActions = mvc.perform(put(ENDPOINT)
                        .contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getUsername()).isEqualTo(vm.getUsername());
        assertThat(resultDTO.getInfo().getFirstname()).isEqualTo(vm.getFirstname());
        assertThat(resultDTO.getInfo().getLastname()).isEqualTo(vm.getLastname());
        assertThat(resultDTO.getInfo().getLanguage()).isEqualTo(Language.valueOf(vm.getLanguage()));
        assertThat(resultDTO.getInfo().getGender()).isEqualTo(Info.Gender.FEMALE);
        assertThat(resultDTO.getInfo().getImageFilename()).isEqualTo(vm.getImageFilename());
    }

    @Test
    @WithAnonymousUser
    void testUpdate_unauthorized() throws Exception {
        UserVM vm = TestUserVM.defaultBuilder().id(CURRENT_ID).build();
        MultiValueMap<String, String> multiValueMap = TestUtils.objectToMap(vm);
        mvc.perform(put(ENDPOINT)
                        .contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext
    void testUpdate_forbidden_wrongUser() throws Exception {
        UserVM vm = TestUserVM.defaultBuilder().id(CURRENT_ID).build();
        MultiValueMap<String, String> multiValueMap = TestUtils.objectToMap(vm);
        mvc.perform(put(ENDPOINT)
                        .contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(id = "bafc4e0e-75d4-4059-9d4d-209855dd91c1")
    void testUpdate_badRequest_notExists() throws Exception {
        UserVM vm = TestUserVM.defaultBuilder().id(UUID.fromString("bafc4e0e-75d4-4059-9d4d-209855dd91c1")).build();
        MultiValueMap<String, String> multiValueMap = TestUtils.objectToMap(vm);
        mvc.perform(put(ENDPOINT)
                        .contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testChangePassword_ok() throws Exception {
        ChangePasswordVM vm = TestChangePasswordVM.defaultBuilder().oldPassword("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/password";
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testChangePassword_unauthorized() throws Exception {
        ChangePasswordVM vm = TestChangePasswordVM.defaultBuilder().oldPassword("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/password";
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "71afdeec-cffd-479f-90ca-12fca4167cda")
    void testChangePassword_wrongProvider() throws Exception {
        ChangePasswordVM vm = TestChangePasswordVM.defaultBuilder().oldPassword("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/password";
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testChangePassword_wrongPassword() throws Exception {
        ChangePasswordVM vm = TestChangePasswordVM.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/password";
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testChangeLanguage_ok() throws Exception {
        ChangeLanguageVM vm = new ChangeLanguageVM("RU");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/language";
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testChangeLanguage_badRequest() throws Exception {
        ChangeLanguageVM vm = new ChangeLanguageVM("wrong");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/language";
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void testChangeLanguage_unauthorized() throws Exception {
        ChangeLanguageVM vm = new ChangeLanguageVM("RU");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/language";
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

}
