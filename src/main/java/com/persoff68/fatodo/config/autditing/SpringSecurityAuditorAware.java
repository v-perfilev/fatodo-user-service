package com.persoff68.fatodo.config.autditing;

import com.persoff68.fatodo.config.constant.AppConstants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(getCurrentUsername().orElse(AppConstants.SYSTEM_USER));
    }

    private static Optional<String> getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = fetchUsernameFromAuthentication(authentication);
        return Optional.ofNullable(username);
    }

    private static String fetchUsernameFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        String username = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) principal;
            username = springSecurityUser.getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }
        return username;
    }
}
