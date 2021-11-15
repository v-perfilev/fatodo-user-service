package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.web.rest.vm.UserVM;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TestUserVM extends UserVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestUserVM(@NotNull UUID id,
                      @NotNull String username,
                      String firstname,
                      String lastname,
                      @NotNull String language,
                      @NotNull String timezone,
                      String imageFilename,
                      MultipartFile imageContent) {
        super(id, username, firstname, lastname, language, timezone, imageFilename, imageContent);
    }

    public static TestUserVMBuilder defaultBuilder() {
        return TestUserVM.builder()
                .id(UUID.randomUUID())
                .username(DEFAULT_VALUE)
                .firstname(DEFAULT_VALUE)
                .lastname(DEFAULT_VALUE)
                .language("en")
                .timezone("Europe/Berlin")
                .imageFilename(DEFAULT_VALUE);
    }

}
