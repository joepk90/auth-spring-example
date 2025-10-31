package com.springauthapi.authservice.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ResourcesSecurityRules implements SecurityRules {
    String routePath = "/resources";

    @Override
    public void configure(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry
                .requestMatchers(HttpMethod.GET, routePath + "/**").permitAll()        
                .requestMatchers(HttpMethod.POST, routePath + "/**").permitAll()  
                .requestMatchers(HttpMethod.PUT, routePath + "/**").denyAll()
                .requestMatchers(HttpMethod.DELETE, routePath + "/**").denyAll();
    }
}
