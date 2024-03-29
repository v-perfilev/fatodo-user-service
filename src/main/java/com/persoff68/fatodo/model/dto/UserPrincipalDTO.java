package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.Info;
import com.persoff68.fatodo.model.Settings;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPrincipalDTO extends AbstractDTO {

    private String email;

    private String username;

    private Set<String> authorities;

    private String password;

    private String provider;

    private String providerId;

    private boolean activated;

    private boolean deleted;

    private Info info;

    private Settings settings;

}
