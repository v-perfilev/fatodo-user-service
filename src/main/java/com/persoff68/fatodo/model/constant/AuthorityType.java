package com.persoff68.fatodo.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AuthorityType {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    @Getter
    private String name;
}
