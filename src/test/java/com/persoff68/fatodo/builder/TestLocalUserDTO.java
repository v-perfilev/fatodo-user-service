package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.LocalUserDTO;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestLocalUserDTO extends LocalUserDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestLocalUserDTO(@NotNull @Email @Size(min = 5, max = 50) String email, @NotNull @Size(min = 5, max = 50) String username, String language, @NotNull @Size(min = 5, max = 100) String password) {
        super(email, username, language, password);
    }

    public static TestLocalUserDTOBuilder defaultBuilder() {
        return TestLocalUserDTO.builder()
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .language("en")
                .password(DEFAULT_VALUE);
    }

}
