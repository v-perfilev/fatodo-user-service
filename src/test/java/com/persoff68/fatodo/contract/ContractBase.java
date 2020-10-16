package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
public class ContractBase {

    @Autowired
    WebApplicationContext context;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;

    @MockBean
    ImageServiceClient imageServiceClient;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
        userRepository.deleteAll();
        User currentUser = FactoryUtils.createUser_local("current", "encodedPassword");
        currentUser.setId("3");
        userRepository.save(currentUser);
        userRepository.save(FactoryUtils.createUser_local("local", "$2a$10$GZrq9GxkRWW1Pv7fKJHGAe4ebib6113zhlU4nZlCtH/ylebR9rkn6"));
        userRepository.save(FactoryUtils.createUser_oAuth2("google", "GOOGLE"));

        when(imageServiceClient.createUserImage(any())).thenReturn("filename");
        when(imageServiceClient.updateUserImage(any())).thenReturn("filename");
        doNothing().when(imageServiceClient).deleteGroupImage(any());
    }

}
