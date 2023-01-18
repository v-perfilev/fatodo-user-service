package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.Info;
import com.persoff68.fatodo.model.Notifications;
import com.persoff68.fatodo.model.Settings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends AbstractAuditingDTO {

    @NotNull
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @NotNull
    @Size(min = 5, max = 50)
    private String username;

    private Set<String> authorities;

    private String provider;

    private String providerId;

    private boolean activated;

    private boolean deleted;

    @NotNull
    private Info info;

    @NotNull
    private Settings settings;

    @NotNull
    private Notifications notifications;

}
