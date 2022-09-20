package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.LocalUserDTO;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestLocalUserDTO extends LocalUserDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestLocalUserDTO(@NotNull @Email @Size(min = 5, max = 50) String email,
                     @NotNull @Size(min = 5, max = 50) String username,
                     @NotNull String language,
                     @NotNull String timezone,
                     @NotNull @Size(min = 5, max = 100) String password) {
        super(email, username, language, timezone, password);
    }

    public static TestLocalUserDTOBuilder defaultBuilder() {
        return TestLocalUserDTO.builder()
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .language("EN")
                .timezone(DEFAULT_VALUE)
                .password(DEFAULT_VALUE);
    }

}
