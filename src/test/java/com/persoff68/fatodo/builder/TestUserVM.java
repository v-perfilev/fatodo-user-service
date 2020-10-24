package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.web.rest.vm.UserVM;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TestUserVM extends UserVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestUserVM(@NotNull UUID id, @NotNull String username, @NotNull String language, String imageFilename, MultipartFile imageContent) {
        super(id, username, language, imageFilename, imageContent);
    }

    public static TestUserVMBuilder defaultBuilder() {
        return TestUserVM.builder()
                .id(UUID.randomUUID())
                .username(DEFAULT_VALUE)
                .language("en");
    }

}
