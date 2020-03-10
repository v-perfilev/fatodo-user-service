package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.repository.UserRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
public class AuthMainBase {

    @Autowired
    WebApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
        userRepository.deleteAll();
        userRepository.save(FactoryUtils.createUser_local("local"));
        userRepository.save(FactoryUtils.createUser_oAuth2("google", "GOOGLE"));
        userRepository.save(FactoryUtils.createUser_oAuth2("facebook", "FACEBOOK"));
    }


}
