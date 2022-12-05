package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestLocalUserDTO;
import com.persoff68.fatodo.builder.TestOAuth2UserDTO;
import com.persoff68.fatodo.builder.TestResetPasswordDTO;
import com.persoff68.fatodo.builder.TestUser;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.constant.Provider;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
@AutoConfigureMockMvc
class SystemControllerIT {
    private static final String ENDPOINT = "/api/system";

    private static final UUID CURRENT_ID = UUID.fromString("6e3c489b-a4fb-4654-aa39-30985b7c4656");
    private static final UUID ACTIVATED_ID = UUID.fromString("24fcc992-a572-458d-b82f-ba34f1f2d0d1");
    private static final UUID LOCAL_ID = UUID.fromString("556d519d-7f84-4b11-bfcd-4fdc87f247b0");
    private static final String CURRENT_NAME = "current-name";
    private static final String ACTIVATED_NAME = "activated-name";
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

    @BeforeEach
    void setup() {
        User currentUser = TestUser.defaultBuilder()
                .id(CURRENT_ID)
                .username(CURRENT_NAME)
                .email(CURRENT_NAME + "@email.com")
                .activated(false)
                .build();

        User activatedUser = TestUser.defaultBuilder()
                .id(ACTIVATED_ID)
                .username(ACTIVATED_NAME)
                .email(ACTIVATED_NAME + "@email.com")
                .build();

        User localUser = TestUser.defaultBuilder()
                .id(LOCAL_ID)
                .username(LOCAL_NAME)
                .email(LOCAL_NAME + "@email.com")
                .build();

        User googleUser = TestUser.defaultBuilder()
                .username(GOOGLE_NAME)
                .email(GOOGLE_NAME + "@email.com")
                .provider(Provider.GOOGLE)
                .build();

        userRepository.save(currentUser);
        userRepository.save(activatedUser);
        userRepository.save(localUser);
        userRepository.save(googleUser);
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalById_ok() throws Exception {
        UUID id = LOCAL_ID;
        String url = ENDPOINT + "/principal/" + id + "/id";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getId()).isEqualTo(id);
    }

    @Test
    @WithAnonymousUser
    void testGetUserPrincipalById_unauthorized() throws Exception {
        String url = ENDPOINT + "/principal/" + LOCAL_ID + "/id";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testGetUserPrincipalById_forbidden() throws Exception {
        String url = ENDPOINT + "/principal/" + LOCAL_ID + "/id";
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalById_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        String url = ENDPOINT + "/principal/" + id + "/id";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalByEmail_ok() throws Exception {
        String email = LOCAL_NAME + "@email.com";
        String url = ENDPOINT + "/principal/" + email + "/email";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getEmail()).isEqualTo(email);
    }

    @Test
    @WithAnonymousUser
    void testGetUserPrincipalByEmail_unauthorized() throws Exception {
        String email = LOCAL_NAME + "@email.com";
        String url = ENDPOINT + "/principal/" + email + "/email";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testGetUserPrincipalByEmail_forbidden() throws Exception {
        String email = LOCAL_NAME + "@email.com";
        String url = ENDPOINT + "/principal/" + email + "/email";
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalByEmail_notFound() throws Exception {
        String email = "not-exists@email.com";
        String url = ENDPOINT + "/principal/" + email + "/email";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalByUsername_ok() throws Exception {
        String username = LOCAL_NAME;
        String url = ENDPOINT + "/principal/" + username + "/username";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getUsername()).isEqualTo(username);
    }

    @Test
    @WithAnonymousUser
    void testGetUserPrincipalByUsername_unauthorized() throws Exception {
        String url = ENDPOINT + "/principal/" + LOCAL_NAME + "/username";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testGetUserPrincipalByUsername_forbidden() throws Exception {
        String url = ENDPOINT + "/principal/" + LOCAL_NAME + "/username";
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalByUsername_notFound() throws Exception {
        String username = "not_exists";
        String url = ENDPOINT + "/principal/" + username + "/username";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalByUsernameOrEmail_ok_username() throws Exception {
        String username = LOCAL_NAME;
        String url = ENDPOINT + "/principal/" + username + "/username-or-email";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getUsername()).isEqualTo(username);
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalByUsernameOrEmail_ok_email() throws Exception {
        String email = LOCAL_NAME + "@email.com";
        String url = ENDPOINT + "/principal/" + email + "/username-or-email";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getEmail()).isEqualTo(email);
    }

    @Test
    @WithAnonymousUser
    void testGetUserPrincipalByUsernameOrEmail_unauthorized() throws Exception {
        String url = ENDPOINT + "/principal/" + LOCAL_NAME + "username-or-email";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testGetUserPrincipalByUsernameOrEmail_forbidden() throws Exception {
        String url = ENDPOINT + "/principal/" + LOCAL_NAME + "username-or-email";
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetUserPrincipalByUsernameOrEmail_notFound() throws Exception {
        String username = "not_exists";
        String url = ENDPOINT + "/principal/" + username + "/username-or-email";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testGetAllDataByIds_ok() throws Exception {
        String url = ENDPOINT + "/data?ids=" + CURRENT_ID;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class,
                UserDTO.class);
        List<UserDTO> userDTOList = objectMapper.readValue(resultString, collectionType);
        assertThat(userDTOList).hasSize(1);
        assertThat(userDTOList.get(0).isDeleted()).isFalse();
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testGetAllDataByIds_forbidden() throws Exception {
        String url = ENDPOINT + "/data?ids=" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testGetAllDataByIds_unauthorized() throws Exception {
        String url = ENDPOINT + "/data?ids=" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testCreateLocalUser_created() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = TestLocalUserDTO.defaultBuilder().email("new-name@email.com").username("new-name").build();
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getPassword()).isNotEmpty();
        assertThat(resultDTO.getProvider()).isEqualTo(Provider.LOCAL.getValue());
        assertThat(resultDTO.getAuthorities()).containsOnly(AuthorityType.USER.getValue());
        assertThat(resultDTO.isActivated()).isFalse();
        assertThat(resultDTO.getSettings().getLanguage()).hasToString(dto.getLanguage());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testCreateLocalUser_conflict_duplicated() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = TestLocalUserDTO.defaultBuilder().username(LOCAL_NAME).build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testCreateLocalUser_badRequest_invalid() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = new LocalUserDTO();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void testCreateLocalUser_unauthorized() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = TestLocalUserDTO.defaultBuilder().email("new-name@email.com").username("new-name").build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testCreateLocalUser_forbidden() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = TestLocalUserDTO.defaultBuilder().email("new-name@email.com").username("new-name").build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testCreateOAuth2User_created() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = TestOAuth2UserDTO.defaultBuilder()
                .email("not-exists@email.com")
                .username("not-exists")
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getPassword()).isNull();
        assertThat(resultDTO.getProvider()).isEqualTo(dto.getProvider());
        assertThat(resultDTO.getProviderId()).isEqualTo(dto.getProviderId());
        assertThat(resultDTO.getAuthorities()).containsOnly(AuthorityType.USER.getValue());
        assertThat(resultDTO.isActivated()).isTrue();
        assertThat(resultDTO.getSettings().getLanguage()).hasToString(dto.getLanguage());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testCreateOAuth2User_conflict_duplicated() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = TestOAuth2UserDTO.defaultBuilder()
                .username(GOOGLE_NAME)
                .email(GOOGLE_NAME + "@email.com")
                .build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testCreateOAuth2User_badRequest_invalid() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = new OAuth2UserDTO();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void testCreateOAuth2User_unauthorized() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = TestOAuth2UserDTO.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testCreateOAuth2User_forbidden() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = TestOAuth2UserDTO.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testActivate_ok() throws Exception {
        String url = ENDPOINT + "/activate/" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testActivate_conflict() throws Exception {
        String url = ENDPOINT + "/activate/" + ACTIVATED_ID;
        mvc.perform(get(url))
                .andExpect(status().isConflict());
    }

    @Test
    @WithAnonymousUser
    void testActivate_unauthorized() throws Exception {
        String url = ENDPOINT + "/activate/" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testActivate_forbidden() throws Exception {
        String url = ENDPOINT + "/activate/" + CURRENT_ID;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testResetPassword_ok() throws Exception {
        ResetPasswordDTO dto = TestResetPasswordDTO.defaultBuilder().userId(CURRENT_ID).build();
        String requestBody = objectMapper.writeValueAsString(dto);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        User result = userRepository.findById(CURRENT_ID).orElse(new User());
        assertThat(result.getPassword()).isEqualTo(dto.getPassword());
    }

    @Test
    @WithAnonymousUser
    void testResetPassword_unauthorized() throws Exception {
        ResetPasswordDTO dto = TestResetPasswordDTO.defaultBuilder().userId(CURRENT_ID).build();
        String requestBody = objectMapper.writeValueAsString(dto);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testResetPassword_forbidden() throws Exception {
        ResetPasswordDTO dto = TestResetPasswordDTO.defaultBuilder().userId(CURRENT_ID).build();
        String requestBody = objectMapper.writeValueAsString(dto);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
