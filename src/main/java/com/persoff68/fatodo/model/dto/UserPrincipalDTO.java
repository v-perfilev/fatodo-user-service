package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPrincipalDTO extends AbstractDTO {

    private String id;
    private String email;
    private String username;
    private String password;
    private String provider;
    private String providerId;
    private Set<String> authorities;
    private String language;
    private boolean activated;

}
