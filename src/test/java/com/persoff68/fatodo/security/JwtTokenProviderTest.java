package com.persoff68.fatodo.security;

import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JwtTokenProvider.class})
@EnableConfigurationProperties(AppProperties.class)
public class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    void testGetAuthenticationFromJwt() {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwidXNlcm5hbWUiOiJ0ZXN0X3VzZXIiLCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlhdCI6MCwiZXhwIjozMjUwMzY3NjQwMH0.ggV38p_Fnqo2OZNtwR3NWKZhMXPd-vf4PrRxN0NmTWsHPrKwWZJSGO2dJBBPWXWs4OI6tjsNV2TM3Kf6NK92hw";
        var authorityList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User("test_user", "", authorityList);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, jwt, authorityList);
        Authentication resultAuthentication = jwtTokenProvider.getAuthenticationFromJwt(jwt);
        assertThat(resultAuthentication).isEqualTo(authentication);
    }

    @Test
    void testCreateSystemJwtAndGetUserIdFromJwt() {
        String jwt = jwtTokenProvider.createSystemJwt();
        String id = jwtTokenProvider.getUserIdFromJwt(jwt);
        assertThat(id).isEqualTo("0");
    }

    @Test
    void testCreateUserJwtAndValidateJwt() {
        var authorityList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User("test_user", "", authorityList);
        String jwt = jwtTokenProvider.createUserJwt("test_id", user);
        boolean isValid = jwtTokenProvider.validateJwt(jwt);
        assertThat(isValid).isTrue();
    }

}
