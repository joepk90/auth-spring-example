package com.springauthapi.authservice.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import com.springauthapi.authservice.user.Role;

@Component
public class UserSecurityRules implements SecurityRules {

    @Override
    public void configure(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/**").permitAll() // in a normal app this should be authorized
                .requestMatchers(HttpMethod.PUT, "/users/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole(Role.ADMIN.name());
    }
}
