package com.persoff68.fatodo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalUserDTO {

    @NotNull
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @NotNull
    @Size(min = 5, max = 50)
    private String username;

    @NotNull
    private String language;

    @NotNull
    private String timezone;

    @NotNull
    @Size(min = 5, max = 100)
    private String password;

}
