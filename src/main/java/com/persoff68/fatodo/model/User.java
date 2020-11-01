package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Document(collection = "ftd_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractAuditingModel {

    @NotNull
    @Indexed(unique = true)
    private String email;

    @NotNull
    @Indexed(unique = true)
    private String username;

    @NotNull
    private Set<Authority> authorities;

    private String password;

    @NotNull
    private Provider provider;

    private String providerId;

    @NotNull
    private boolean activated;

    @NotNull
    private Info info = new Info();

}
