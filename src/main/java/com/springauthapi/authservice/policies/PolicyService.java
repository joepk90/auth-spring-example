package com.springauthapi.authservice.policies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import java.lang.String;

@Service
public class PolicyService {
    
    @Value("${policy-service.base-url}")
    private String policyServiceBaseUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
            this.webClient = WebClient.builder()
                            .baseUrl(policyServiceBaseUrl)
                            .build();

        //     System.out.println("✅ WebClient initialized with base URL: " + policyServiceBaseUrl);
    }
}
