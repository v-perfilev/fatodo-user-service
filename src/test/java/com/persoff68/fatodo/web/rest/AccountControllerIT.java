package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.TestUtils;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestChangePasswordVM;
import com.persoff68.fatodo.builder.TestInfoVM;
import com.persoff68.fatodo.builder.TestNotificationsVM;
import com.persoff68.fatodo.builder.TestSettingsVM;
import com.persoff68.fatodo.builder.TestUser;
import com.persoff68.fatodo.client.ChatSystemServiceClient;
import com.persoff68.fatodo.client.ContactSystemServiceClient;
import com.persoff68.fatodo.client.EventSystemServiceClient;
import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.client.ItemSystemServiceClient;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.constant.EmailNotificationType;
import com.persoff68.fatodo.model.constant.Provider;
import com.persoff68.fatodo.model.constant.PushNotificationType;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.vm.ChangeLanguageVM;
import com.persoff68.fatodo.model.vm.ChangePasswordVM;
import com.persoff68.fatodo.model.vm.InfoVM;
import com.persoff68.fatodo.model.vm.NotificationsVM;
import com.persoff68.fatodo.model.vm.SettingsVM;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
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
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    ChatSystemServiceClient chatSystemServiceClient;
    @MockBean
    ContactSystemServiceClient contactSystemServiceClient;
    @MockBean
    EventSystemServiceClient eventSystemServiceClient;
    @MockBean
    ImageServiceClient imageServiceClient;
    @MockBean
    ItemSystemServiceClient itemSystemServiceClient;

    @BeforeEach
    void setup() {
        User currentUser = TestUser.defaultBuilder().id(CURRENT_ID).username(CURRENT_NAME).email(CURRENT_NAME +
                "@email.com").password(passwordEncoder.encode("test_password")).build();

        User localUser =
                TestUser.defaultBuilder().username(LOCAL_NAME).email(LOCAL_NAME + "@email.com").password(passwordEncoder.encode("test_password")).build();

        User googleUser = TestUser.defaultBuilder().id(GOOGLE_ID).username(GOOGLE_NAME).email(GOOGLE_NAME + "@email" +
                ".com").provider(Provider.GOOGLE).build();

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
        ResultActions resultActions = mvc.perform(get(ENDPOINT)).andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isEqualTo(CURRENT_ID);
    }

    @Test
    @WithAnonymousUser
    void testGetCurrentUser_unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testUpdateInfo_ok() throws Exception {
        InfoVM vm = TestInfoVM.defaultBuilder()
                .username("test_username")
                .firstname("test_firstname")
                .lastname("test_lastname")
                .gender("DIVERSE")
                .build();
        MultiValueMap<String, String> multiValueMap = TestUtils.objectToMap(vm);
        String url = ENDPOINT + "/info";
        mvc.perform(put(url).contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap)).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testUpdateInfo_unauthorized() throws Exception {
        InfoVM vm = TestInfoVM.defaultBuilder().build();
        MultiValueMap<String, String> multiValueMap = TestUtils.objectToMap(vm);
        String url = ENDPOINT + "/info";
        mvc.perform(put(url).contentType(MediaType.MULTIPART_FORM_DATA).params(multiValueMap)).andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testUpdateSettings_ok() throws Exception {
        SettingsVM vm = TestSettingsVM.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/settings";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testUpdateSettings_unauthorized() throws Exception {
        SettingsVM vm = TestSettingsVM.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/settings";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testUpdateNotifications_ok() throws Exception {
        NotificationsVM vm = TestNotificationsVM.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/notifications";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());
        User user = userRepository.findById(CURRENT_ID).orElseThrow();
        assertThat(user.getNotifications().getEmailNotifications())
                .contains(EmailNotificationType.REMINDER);
        assertThat(user.getNotifications().getPushNotifications())
                .contains(PushNotificationType.REMINDER)
                .hasSizeGreaterThan(5);
    }

    @Test
    @WithAnonymousUser
    void testUpdateNotifications_unauthorized() throws Exception {
        NotificationsVM vm = TestNotificationsVM.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/notifications";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testChangePassword_ok() throws Exception {
        ChangePasswordVM vm = TestChangePasswordVM.defaultBuilder().oldPassword("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/password";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());
    }

    @Test
    @WithCustomSecurityContext(id = "71afdeec-cffd-479f-90ca-12fca4167cda")
    void testChangePassword_wrongProvider() throws Exception {
        ChangePasswordVM vm = TestChangePasswordVM.defaultBuilder().oldPassword("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/password";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void testChangePassword_unauthorized() throws Exception {
        ChangePasswordVM vm = TestChangePasswordVM.defaultBuilder().oldPassword("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/password";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testChangePassword_wrongPassword() throws Exception {
        ChangePasswordVM vm = TestChangePasswordVM.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/password";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());
    }


    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testChangeLanguage_ok() throws Exception {
        ChangeLanguageVM vm = new ChangeLanguageVM("RU");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/language";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testChangeLanguage_unauthorized() throws Exception {
        ChangeLanguageVM vm = new ChangeLanguageVM("RU");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/language";
        mvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(id = "6e3c489b-a4fb-4654-aa39-30985b7c4656")
    void testDeleteAccountPermanently() throws Exception {
        mvc.perform(delete(ENDPOINT)).andExpect(status().isOk());

        verify(chatSystemServiceClient, timeout(1000)).deleteAccountPermanently(CURRENT_ID);
        verify(contactSystemServiceClient, timeout(1000)).deleteAccountPermanently(CURRENT_ID);
        verify(eventSystemServiceClient, timeout(1000)).deleteAccountPermanently(CURRENT_ID);
        verify(itemSystemServiceClient, timeout(1000)).deleteAccountPermanently(CURRENT_ID);

        User user = userRepository.findById(CURRENT_ID).orElseThrow(ModelNotFoundException::new);

        assertThat(user.getEmail()).isEqualTo(CURRENT_ID.toString());
        assertThat(user.getUsername()).isEqualTo(CURRENT_ID.toString());
        assertThat(user.getPassword()).isEqualTo(CURRENT_ID.toString());
        assertThat(user.isDeleted()).isTrue();
    }

}
