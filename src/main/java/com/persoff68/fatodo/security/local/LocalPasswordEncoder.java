package com.persoff68.fatodo.security.local;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component("passwordEncoder")
public class LocalPasswordEncoder extends BCryptPasswordEncoder {

    public LocalPasswordEncoder() {
        super(10);
    }
}
