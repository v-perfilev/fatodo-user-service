package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.vm.UserVM;
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
                      String gender,
                      @NotNull String language,
                      @NotNull String timezone,
                      String timeFormat,
                      String dateFormat,
                      String imageFilename,
                      MultipartFile imageContent,
                      boolean emailReminders) {
        super(id, username, firstname, lastname, gender, language, timezone, timeFormat, dateFormat, imageFilename,
                imageContent, emailReminders);
    }

    public static TestUserVMBuilder defaultBuilder() {
        return TestUserVM.builder()
                .id(UUID.randomUUID())
                .username(DEFAULT_VALUE)
                .firstname(DEFAULT_VALUE)
                .lastname(DEFAULT_VALUE)
                .gender("FEMALE")
                .language("EN")
                .timezone("Europe/Berlin")
                .timeFormat("H24")
                .dateFormat("MDY_SLASH")
                .imageFilename(DEFAULT_VALUE);
    }

}
