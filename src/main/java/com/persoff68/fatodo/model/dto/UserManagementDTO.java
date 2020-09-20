package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserManagementDTO extends AbstractAuditingDTO {

    @NotNull
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @NotNull
    @Size(min = 5, max = 50)
    private String username;

    private Set<String> authorities;

    private String language;

    private String imageFilename;

    private String provider;

    private String providerId;

    private boolean activated;

}
