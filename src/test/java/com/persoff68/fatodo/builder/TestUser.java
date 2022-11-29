package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.Info;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.constant.Gender;
import com.persoff68.fatodo.model.constant.Language;
import com.persoff68.fatodo.model.constant.Provider;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class TestUser extends User {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestUser(UUID id, @NotNull String email, @NotNull String username, @NotNull Set<Authority> authorities,
                    String password, @NotNull Provider provider, String providerId, @NotNull boolean activated,
                    @NotNull boolean deleted, @NotNull Info info) {
        super(email, username, authorities, password, provider, providerId, activated, deleted, info);
        super.setId(id);
        super.setInfo(new Info());
        super.getInfo().setFirstname(DEFAULT_VALUE);
        super.getInfo().setLastname(DEFAULT_VALUE);
        super.getInfo().setLanguage(Language.EN);
        super.getInfo().setGender(Gender.FEMALE);
        super.getInfo().setImageFilename(DEFAULT_VALUE);
    }

    public static TestUserBuilder defaultBuilder() {
        return TestUser.builder()
                .id(UUID.randomUUID())
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .authorities(Collections.singleton(new Authority(AuthorityType.USER.getValue())))
                .password(DEFAULT_VALUE)
                .provider(Provider.LOCAL)
                .activated(true)
                .deleted(false);
    }

}
