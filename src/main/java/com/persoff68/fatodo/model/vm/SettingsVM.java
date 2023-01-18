package com.persoff68.fatodo.model.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsVM {

    @NotNull
    private String language;

    @NotNull
    private String timezone;

    @NotNull
    private String timeFormat;

    @NotNull
    private String dateFormat;

}
