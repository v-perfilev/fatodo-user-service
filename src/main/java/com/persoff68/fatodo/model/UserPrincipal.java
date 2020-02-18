package com.persoff68.fatodo.model;

import lombok.Data;

import java.util.Set;

@Data
public class UserPrincipal {
    private static final long serialVersionUID = 1L;

    private String id;
    private String email;
    private String username;
    private String password;
    private String provider;
    private String providerId;
    private Set<String> authorities;

}
