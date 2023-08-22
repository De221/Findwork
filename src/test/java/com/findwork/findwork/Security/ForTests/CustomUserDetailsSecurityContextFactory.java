package com.findwork.findwork.Security.ForTests;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.UUID;


public class CustomUserDetailsSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser>
{
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new CustomAuthentication(UUID.fromString(annotation.id()), annotation.username(), annotation.password());
        context.setAuthentication(authentication);
        return context;
    }
}
