package com.persoff68.fatodo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Document(collection = "ftd_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractAuditingDocument {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String email;

    @NotNull
    private String username;

    private String password;

    private Set<Authority> authorities;

    @NotNull
    private String provider;

    @Field("provider_id")
    private String providerId;
}
