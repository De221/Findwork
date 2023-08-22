package com.findwork.findwork.Security.ForTests;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Annotation;
import java.util.UUID;

@WithSecurityContext(factory = CustomUserDetailsSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String id() default "0000-00-00-00-000000";
    String username() default "username";
    String password() default "password";
}
