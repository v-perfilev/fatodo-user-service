package com.persoff68.fatodo.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class OAuth2UserDTO {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotNull
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @NotNull
    @Size(min = 5, max = 50)
    private String username;

    @NotNull
    private String provider;

    @NotNull
    private String providerId;

}
