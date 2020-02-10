package com.persoff68.fatodo.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum AuthorityType {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    @Getter
    private String name;

    public static boolean contains(String name) {
        return Stream.of(AuthorityType.values()).map(AuthorityType::getName).anyMatch(n -> n.equals(name));
    }
}
