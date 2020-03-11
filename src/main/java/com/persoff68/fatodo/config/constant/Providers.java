package com.persoff68.fatodo.config.constant;

public interface Providers {
    String LOCAL = "LOCAL";
    String GOOGLE = "GOOGLE";
    String FACEBOOK = "FACEBOOK";

    String[] allProviders = {LOCAL, GOOGLE, FACEBOOK};

    static boolean contains(String provider) {
        for (String p : allProviders) {
            if (p.equals(provider)) {
                return true;
            }
        }
        return false;
    }
}
