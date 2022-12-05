package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.vm.SettingsVM;
import lombok.Builder;

import javax.validation.constraints.NotNull;

public class TestSettingsVM extends SettingsVM {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestSettingsVM(@NotNull String language,
                          @NotNull String timezone,
                          String timeFormat,
                          String dateFormat,
                          boolean emailReminders) {
        super(language, timezone, timeFormat, dateFormat, emailReminders);
    }

    public static TestSettingsVMBuilder defaultBuilder() {
        return TestSettingsVM.builder()
                .language("EN")
                .timezone("Europe/Berlin")
                .timeFormat("H24")
                .dateFormat("MDY_SLASH")
                .emailReminders(true);
    }

}
