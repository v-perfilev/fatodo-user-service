package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.Info;
import com.persoff68.fatodo.model.Notifications;
import com.persoff68.fatodo.model.Settings;
import com.persoff68.fatodo.model.constant.Language;
import com.persoff68.fatodo.model.constant.Provider;
import com.persoff68.fatodo.model.dto.UserDTO;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class TestUserDTO extends UserDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestUserDTO(UUID id, @NotNull @Email @Size(min = 5, max = 50) String email, @NotNull @Size(min = 5, max =
            50) String username, Set<String> authorities, String provider, String providerId, boolean activated,
                       boolean deleted, Info info, Settings settings, Notifications notifications) {
        super(email, username, authorities, provider, providerId, activated, deleted, info, settings, notifications);
        super.setId(id);
        super.setInfo(new Info());
        super.getInfo().setFirstname(DEFAULT_VALUE);
        super.getInfo().setLastname(DEFAULT_VALUE);
        super.getInfo().setImageFilename(DEFAULT_VALUE);
        super.setSettings(new Settings());
        super.getSettings().setLanguage(Language.EN);
        super.setNotifications(new Notifications());
    }

    public static TestUserDTOBuilder defaultBuilder() {
        return TestUserDTO.builder()
                .id(UUID.randomUUID())
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .authorities(Collections.singleton(AuthorityType.USER.getValue()))
                .provider(Provider.LOCAL.getValue())
                .activated(true);
    }

}
