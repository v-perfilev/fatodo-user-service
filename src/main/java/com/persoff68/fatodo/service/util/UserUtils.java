package com.persoff68.fatodo.service.util;

import java.util.regex.Pattern;

public class UserUtils {

    private static final Pattern EMAIL_PATTERN
            = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+"
            + "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");

    private UserUtils() {
    }

    public static boolean isEmail(String input) {
        return input != null && EMAIL_PATTERN.matcher(input).matches();
    }

}
