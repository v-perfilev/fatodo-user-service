package com.persoff68.fatodo.annotation;

import com.persoff68.fatodo.security.details.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomSecurityContext.WithCustomSecurityContextFactory.class)
public @interface WithCustomSecurityContext {

    String id() default "test_id";

    String username() default "test_username";

    String jwt() default "test_jwt";

    String authority() default "ROLE_TEST";

    class WithCustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomSecurityContext> {
        @Override
        public SecurityContext createSecurityContext(WithCustomSecurityContext customSecurityContext) {
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            List<? extends GrantedAuthority> authorityList =
                    Collections.singletonList(new SimpleGrantedAuthority(customSecurityContext.authority()));

            CustomUserDetails userDetails = new CustomUserDetails(customSecurityContext.id(),
                    customSecurityContext.username(), "", authorityList);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    customSecurityContext.jwt(), authorityList);

            securityContext.setAuthentication(authentication);
            return securityContext;
        }
    }
}

