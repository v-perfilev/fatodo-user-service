package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.config.constant.Authorities;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

@Document(collection = "ftd_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractAuditingModel {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @Id
    private String id = null;

    @NotNull
    @Indexed(unique = true)
    private String email;

    @NotNull
    @Indexed(unique = true)
    private String username;

    private String password;

    @NotNull
    private String provider;

    private String providerId;

    @NotNull
    private Set<Authority> authorities = Collections.singleton(Authority.of(Authorities.USER));
}
