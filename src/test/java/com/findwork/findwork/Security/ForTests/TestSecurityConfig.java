package com.findwork.findwork.Security.ForTests;

import com.findwork.findwork.Security.config.WebSecurityConfig;
import com.findwork.findwork.Services.UserService;
import com.findwork.findwork.Services.ValidationService;
import org.apache.catalina.security.SecurityConfig;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@TestConfiguration
@EnableWebSecurity
@Configuration
public class TestSecurityConfig extends WebSecurityConfig {
    @Autowired
    private UserService userService;
    @MockBean
    private BCryptPasswordEncoder encoder;
    SecurityHandler securityHandler = new SecurityHandler();

    public TestSecurityConfig(UserService userService, BCryptPasswordEncoder encoder) {
        super(userService, encoder);
    }

    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/*").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(securityHandler)
                .permitAll()
                .and()
                .csrf().disable();
        return http.build();
    }
}
