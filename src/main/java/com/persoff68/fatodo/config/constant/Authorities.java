package com.persoff68.fatodo.config.constant;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public interface Authorities {
    String SYSTEM = "ROLE_SYSTEM";
    String ADMIN = "ROLE_ADMIN";
    String USER = "ROLE_USER";

    String[] allRoles = {SYSTEM, ADMIN, USER};

    static boolean contains(String authority) {
        for (String allRole : allRoles) {
            if (allRole.equals(authority)) {
                return true;
            }
        }
        return false;
    }

    static String[] values() {
        return allRoles;
    }

}
