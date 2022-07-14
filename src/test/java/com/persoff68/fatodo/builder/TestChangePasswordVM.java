package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.vm.ChangePasswordVM;
import lombok.Builder;

public class TestChangePasswordVM extends ChangePasswordVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestChangePasswordVM(String oldPassword, String newPassword) {
        super(oldPassword, newPassword);
    }

    public static TestChangePasswordVMBuilder defaultBuilder() {
        return TestChangePasswordVM.builder()
                .oldPassword(DEFAULT_VALUE)
                .newPassword(DEFAULT_VALUE);
    }

}
