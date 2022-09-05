package com.persoff68.fatodo.config.constant;

import java.util.Arrays;

public enum Language {
    EN("EN"),
    RU("RU");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String value) {
        return Arrays.stream(values()).anyMatch(language -> language.getValue().equals(value));
    }

}
