package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.vm.InfoVM;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class TestInfoVM extends InfoVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestInfoVM(@NotNull String username,
                      String firstname,
                      String lastname,
                      String gender,
                      String imageFilename,
                      MultipartFile imageContent) {
        super(username, firstname, lastname, gender, imageFilename, imageContent);
    }

    public static TestInfoVMBuilder defaultBuilder() {
        return TestInfoVM.builder()
                .username(DEFAULT_VALUE)
                .firstname(DEFAULT_VALUE)
                .lastname(DEFAULT_VALUE)
                .gender("FEMALE")
                .imageFilename(DEFAULT_VALUE);
    }

}
