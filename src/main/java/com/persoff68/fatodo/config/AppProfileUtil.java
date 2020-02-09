package com.persoff68.fatodo.config;

import com.persoff68.fatodo.constant.ProfileConstants;

import java.util.Properties;

public class AppProfileUtil {

    private static final String SPRING_PROFILES_DEFAULT = "spring.profiles.default";

    public static Properties getDefaultProfile() {
        String[] profiles = {ProfileConstants.DEVELOPMENT};
        Properties properties = new Properties();
        properties.put(SPRING_PROFILES_DEFAULT, profiles);
        return properties;
    }

}
