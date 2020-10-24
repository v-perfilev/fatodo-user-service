package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestUser;
import com.persoff68.fatodo.client.ImageServiceClient;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.repository.UserRepository;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
public class ContractBase {
    private static final UUID CURRENT_ID = UUID.fromString("8f9a7cae-73c8-4ad6-b135-5bd109b51d2e");
    private static final UUID LOCAL_ID = UUID.fromString("73e6c420-dccc-4d54-99a7-3017af127d8a");
    private static final String CURRENT_NAME = "current-name";
    private static final String LOCAL_NAME = "local-name";
    private static final String GOOGLE_NAME = "google-name";

    @Autowired
    WebApplicationContext context;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    ImageServiceClient imageServiceClient;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);

        User currentUser = TestUser.defaultBuilder()
                .id(CURRENT_ID)
                .username(CURRENT_NAME)
                .email(CURRENT_NAME + "@email.com")
                .password(passwordEncoder.encode("test_password"))
                .build();

        User localUser = TestUser.defaultBuilder()
                .id(LOCAL_ID)
                .username(LOCAL_NAME)
                .email(LOCAL_NAME + "@email.com")
                .password(passwordEncoder.encode("test_password"))
                .activated(false)
                .build();

        User googleUser = TestUser.defaultBuilder()
                .username(GOOGLE_NAME)
                .email(GOOGLE_NAME + "@email.com")
                .provider(Provider.GOOGLE)
                .build();

        userRepository.deleteAll();
        userRepository.save(currentUser);
        userRepository.save(localUser);
        userRepository.save(googleUser);

        when(imageServiceClient.createUserImage(any())).thenReturn("filename");
        when(imageServiceClient.updateUserImage(any())).thenReturn("filename");
        doNothing().when(imageServiceClient).deleteGroupImage(any());
    }

}
