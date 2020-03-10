package com.persoff68.fatodo;

import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;

import java.util.Collections;

public class FactoryUtils {

    public static User createUser_local(String id) {
        User user = new User();
        user.setId("test_id_" + id);
        user.setUsername("test_username_" + id);
        user.setEmail("test_" + id + "@email.com");
        user.setPassword("$2a$10$GZrq9GxkRWW1Pv7fKJHGAe4ebib6113zhlU4nZlCtH/ylebR9rkn6");
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setProvider("LOCAL");
        return user;
    }

    public static User createUser_oAuth2(String id, String provider) {
        User user = new User();
        user.setId("test_id_" + id);
        user.setUsername("test_" + id + "@email.com");
        user.setEmail("test_" + id + "@email.com");
        user.setAuthorities(Collections.singleton(new Authority("ROLE_USER")));
        user.setProvider(provider);
        user.setProviderId("test_id_" + id);
        return user;
    }

}
