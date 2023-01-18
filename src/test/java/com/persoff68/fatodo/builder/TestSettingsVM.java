package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.vm.SettingsVM;
import lombok.Builder;

import javax.validation.constraints.NotNull;

public class TestSettingsVM extends SettingsVM {

    @Builder
    public TestSettingsVM(@NotNull String language,
                          @NotNull String timezone,
                          String timeFormat,
                          String dateFormat) {
        super(language, timezone, timeFormat, dateFormat);
    }

    public static TestSettingsVMBuilder defaultBuilder() {
        return TestSettingsVM.builder()
                .language("EN")
                .timezone("Europe/Berlin")
                .timeFormat("H24")
                .dateFormat("MDY_SLASH");
    }

}
