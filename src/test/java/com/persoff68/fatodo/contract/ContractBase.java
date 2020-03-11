package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.config.constant.Authorities;
import com.persoff68.fatodo.config.constant.Providers;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
public class ContractBase {

    @Autowired
    WebApplicationContext context;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
        userRepository.deleteAll();
        userRepository.save(FactoryUtils.createUser_local("local", "$2a$10$GZrq9GxkRWW1Pv7fKJHGAe4ebib6113zhlU4nZlCtH/ylebR9rkn6"));
        userRepository.save(FactoryUtils.createUser_oAuth2("google", "GOOGLE"));
    }

    protected void assertSystemJwt(String jwt) {
        Authentication authentication = jwtTokenProvider.getAuthenticationFromJwt(jwt);
        List<String> authorityList = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        BDDAssertions.then(authorityList).contains(Authorities.SYSTEM);
    }

    protected void assertAdminJwt(String jwt) {
        Authentication authentication = jwtTokenProvider.getAuthenticationFromJwt(jwt);
        List<String> authorityList = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        BDDAssertions.then(authorityList).contains(Authorities.ADMIN);
    }

    protected void assertAuthorities(List<String> authorities) {
        boolean isValid = true;
        for (String authority : authorities) {
            if (!Authorities.contains(authority)) {
                isValid = false;
                break;
            }
        }
        BDDAssertions.then(isValid).isTrue();
    }

    protected void assertProvider(String provider) {
        boolean isValid = Providers.contains(provider);
        BDDAssertions.then(isValid).isTrue();
    }

}
