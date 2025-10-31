package com.springauthapi.authservice.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ResourcePostSecurityRules implements SecurityRules {
    String routePath = "/resource-post";

    @Override
    public void configure(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers(HttpMethod.GET, routePath + "/**").permitAll()          
                .requestMatchers(HttpMethod.POST, routePath + "/**").denyAll()
                .requestMatchers(HttpMethod.PUT, routePath + "/**").denyAll()
                .requestMatchers(HttpMethod.DELETE, routePath + "/**").denyAll();
    }
}
