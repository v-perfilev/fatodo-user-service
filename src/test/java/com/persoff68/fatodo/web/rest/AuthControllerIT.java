package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.FatodoUserServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
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

import static com.persoff68.fatodo.FactoryUtils.createUser_local;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoUserServiceApplication.class)
public class AuthControllerIT {
    private static final String ENDPOINT = "/api/auth";

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
        User activatedUser = FactoryUtils.createUser_local("activated", "encodedPassword");
        activatedUser.setId("4");
        activatedUser.setActivated(true);
        userRepository.save(activatedUser);
        userRepository.save(createUser_local("local", "encodedPassword"));
        userRepository.save(FactoryUtils.createUser_oAuth2("oauth2", Provider.GOOGLE.getValue()));
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetUserPrincipalById_ok() throws Exception {
        String id = "test_id_local";
        String url = ENDPOINT + "/id/" + id;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getId()).isEqualTo(id);
    }

    @Test
    @WithAnonymousUser
    public void testGetUserPrincipalById_unauthorized() throws Exception {
        String id = "test_id_local";
        String url = ENDPOINT + "/id/" + id;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    public void testGetUserPrincipalById_forbidden() throws Exception {
        String id = "test_id_local";
        String url = ENDPOINT + "/id/" + id;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetUserPrincipalById_notFound() throws Exception {
        String id = "test_id_not_exists";
        String url = ENDPOINT + "/id/" + id;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetUserPrincipalByEmail_ok() throws Exception {
        String email = "test_local@email.com";
        String url = ENDPOINT + "/email/" + email;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getEmail()).isEqualTo(email);
    }

    @Test
    @WithAnonymousUser
    public void testGetUserPrincipalByEmail_unauthorized() throws Exception {
        String email = "test_local@email.com";
        String url = ENDPOINT + "/email/" + email;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    public void testGetUserPrincipalByEmail_forbidden() throws Exception {
        String email = "test_local@email.com";
        String url = ENDPOINT + "/email/" + email;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetUserPrincipalByEmail_notFound() throws Exception {
        String email = "test_not_exists@email.com";
        String url = ENDPOINT + "/email/" + email;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetUserPrincipalByUsername_ok() throws Exception {
        String username = "test_username_local";
        String url = ENDPOINT + "/username/" + username;
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserPrincipalDTO resultDTO = objectMapper.readValue(resultString, UserPrincipalDTO.class);
        assertThat(resultDTO.getUsername()).isEqualTo(username);
    }

    @Test
    @WithAnonymousUser
    public void testGetUserPrincipalByUsername_unauthorized() throws Exception {
        String username = "test_username_local";
        String url = ENDPOINT + "/username/" + username;
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    public void testGetUserPrincipalByUsername_forbidden() throws Exception {
        String username = "test_username_local";
        String url = ENDPOINT + "/username/" + username;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testGetUserPrincipalByUsername_notFound() throws Exception {
        String username = "test_username_not_exists";
        String url = ENDPOINT + "/username/" + username;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testCreateLocalUser_created() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = FactoryUtils.createLocalUserDTO("not_exists");
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
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testCreateLocalUser_conflict_duplicated() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = FactoryUtils.createLocalUserDTO("local");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testCreateLocalUser_badRequest_invalid() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = FactoryUtils.createInvalidLocalUserDTO();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testCreateLocalUser_unauthorized() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = FactoryUtils.createLocalUserDTO("local");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    public void testCreateLocalUser_forbidden() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = FactoryUtils.createLocalUserDTO("local");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testCreateOAuth2User_created() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = FactoryUtils.createOAuth2UserDTO("not_exists", Provider.GOOGLE.getValue());
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
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testCreateOAuth2User_conflict_duplicated() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = FactoryUtils.createOAuth2UserDTO("oauth2", Provider.GOOGLE.getValue());
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testCreateOAuth2User_badRequest_invalid() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = FactoryUtils.createInvalidOAuth2UserDTO();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testCreateOAuth2User_unauthorized() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = FactoryUtils.createOAuth2UserDTO("oauth2", Provider.GOOGLE.getValue());
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    public void testCreateOAuth2User_forbidden() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = FactoryUtils.createOAuth2UserDTO("oauth2", Provider.GOOGLE.getValue());
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testActivate_ok() throws Exception {
        String url = ENDPOINT + "/activate/3";
        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testActivate_conflict() throws Exception {
        String url = ENDPOINT + "/activate/4";
        mvc.perform(get(url))
                .andExpect(status().isConflict());
    }

    @Test
    @WithAnonymousUser
    public void testActivate_unauthorized() throws Exception {
        String url = ENDPOINT + "/activate/3";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    public void testActivate_forbidden() throws Exception {
        String url = ENDPOINT + "/activate/3";
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    public void testResetPassword_ok() throws Exception {
        ResetPasswordDTO dto = FactoryUtils.createResetPasswordDTO("3");
        String requestBody = objectMapper.writeValueAsString(dto);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        User result = userRepository.findById("3").orElse(new User());
        assertThat(result.getPassword()).isEqualTo(dto.getPassword());
    }

    @Test
    @WithAnonymousUser
    public void testResetPassword_unauthorized() throws Exception {
        ResetPasswordDTO dto = FactoryUtils.createResetPasswordDTO("3");
        String requestBody = objectMapper.writeValueAsString(dto);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    public void testResetPassword_forbidden() throws Exception {
        ResetPasswordDTO dto = FactoryUtils.createResetPasswordDTO("3");
        String requestBody = objectMapper.writeValueAsString(dto);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
