package com.persoff68.fatodo.model;

import com.persoff68.fatodo.model.constant.AuthProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Document(collection = "ftd_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractAuditingDocument {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    private String login;

    @NotNull
    private String email;

    private String password;

    @NotNull
    private AuthProvider provider;

    private Set<Authority> authorities;

}
