package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
public class UserAuthBase {

    @Autowired
    WebApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
        userRepository.deleteAll();
        userRepository.save(createUser(1));
    }

    private static User createUser(int id) {
        User user = new User();
        user.setId("test_id_" + id);
        user.setUsername("test_username_" + id);
        user.setEmail("test_" + id + "@email.com");
        user.setPassword("test_password");
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setProvider("LOCAL");
        return user;
    }
}
