package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class TestUser extends User {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestUser(UUID id, @NotNull String email, @NotNull String username, String imageFilename, @NotNull String language, @NotNull Set<Authority> authorities, String password, @NotNull Provider provider, String providerId, @NotNull boolean activated) {
        super(email, username, imageFilename, language, authorities, password, provider, providerId, activated);
        this.setId(id);
    }

    public static TestUserBuilder defaultBuilder() {
        return TestUser.builder()
                .id(UUID.randomUUID())
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .imageFilename(DEFAULT_VALUE)
                .language("en")
                .authorities(Collections.singleton(new Authority(AuthorityType.USER.getValue())))
                .password(DEFAULT_VALUE)
                .provider(Provider.LOCAL)
                .activated(true);
    }

}
