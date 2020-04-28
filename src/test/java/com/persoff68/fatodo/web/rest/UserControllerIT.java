package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoUserServiceApplication;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
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

@SpringBootTest(classes = FaToDoUserServiceApplication.class)
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
        userRepository.save(FactoryUtils.createUser_oAuth2("oauth2", Provider.Constants.GOOGLE_VALUE));
    }

    @Test
    @WithCustomSecurityContext(jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwidXNlcm5hbWUiOiJ0ZXN0X3VzZXIiLCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.ggV38p_Fnqo2OZNtwR3NWKZhMXPd-vf4PrRxN0NmTWsHPrKwWZJSGO2dJBBPWXWs4OI6tjsNV2TM3Kf6NK92hw")
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
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
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
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testGetUserPrincipalById_forbidden() throws Exception {
        String id = "test_id_local";
        String url = ENDPOINT + "/id/" + id;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testGetUserPrincipalById_notFound() throws Exception {
        String id = "test_id_not_exists";
        String url = ENDPOINT + "/id/" + id;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
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
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testGetUserPrincipalByEmail_forbidden() throws Exception {
        String email = "test_local@email.com";
        String url = ENDPOINT + "/email/" + email;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testGetUserPrincipalByEmail_notFound() throws Exception {
        String email = "test_not_exists@email.com";
        String url = ENDPOINT + "/email/" + email;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
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
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testGetUserPrincipalByUsername_forbidden() throws Exception {
        String username = "test_username_local";
        String url = ENDPOINT + "/username/" + username;
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testGetUserPrincipalByUsername_notFound() throws Exception {
        String username = "test_username_not_exists";
        String url = ENDPOINT + "/username/" + username;
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testCreateLocalUser_created() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = FactoryUtils.createLocalUserDTO("not_exists");
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getProvider()).isEqualTo(Provider.Constants.LOCAL_VALUE);
        assertThat(resultDTO.getAuthorities()).containsOnly(AuthorityType.Constants.USER_VALUE);
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testCreateLocalUser_conflict_duplicated() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = FactoryUtils.createLocalUserDTO("local");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
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
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testCreateLocalUser_forbidden() throws Exception {
        String url = ENDPOINT + "/local";
        LocalUserDTO dto = FactoryUtils.createLocalUserDTO("local");
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testCreateOAuth2User_created() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = FactoryUtils.createOAuth2UserDTO("not_exists", Provider.Constants.GOOGLE_VALUE);
        String requestBody = objectMapper.writeValueAsString(dto);
        ResultActions resultActions = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        UserDTO resultDTO = objectMapper.readValue(resultString, UserDTO.class);
        assertThat(resultDTO.getId()).isNotNull();
        assertThat(resultDTO.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resultDTO.getUsername()).isEqualTo(dto.getUsername());
        assertThat(resultDTO.getProvider()).isEqualTo(dto.getProvider());
        assertThat(resultDTO.getProviderId()).isEqualTo(dto.getProviderId());
        assertThat(resultDTO.getAuthorities()).containsOnly(AuthorityType.Constants.USER_VALUE);
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
    public void testCreateOAuth2User_conflict_duplicated() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = FactoryUtils.createOAuth2UserDTO("oauth2", Provider.Constants.GOOGLE_VALUE);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.SYSTEM_VALUE)
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
        OAuth2UserDTO dto = FactoryUtils.createOAuth2UserDTO("oauth2", Provider.Constants.GOOGLE_VALUE);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testCreateOAuth2User_forbidden() throws Exception {
        String url = ENDPOINT + "/oauth2";
        OAuth2UserDTO dto = FactoryUtils.createOAuth2UserDTO("oauth2", Provider.Constants.GOOGLE_VALUE);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
