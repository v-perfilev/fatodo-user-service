package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestOAuth2UserDTO extends OAuth2UserDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestOAuth2UserDTO(@NotNull @Email @Size(min = 5, max = 50) String email,
                      @NotNull @Size(min = 5, max = 50) String username,
                      @NotNull String language,
                      @NotNull String timezone,
                      @NotNull String provider,
                      @NotNull String providerId) {
        super(email, username, language, timezone, provider, providerId);
    }

    public static TestOAuth2UserDTOBuilder defaultBuilder() {
        return TestOAuth2UserDTO.builder()
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .language("en")
                .timezone(DEFAULT_VALUE)
                .provider(Provider.GOOGLE.getValue())
                .providerId(DEFAULT_VALUE);
    }

}
