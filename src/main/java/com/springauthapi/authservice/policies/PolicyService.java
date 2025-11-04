package com.springauthapi.authservice.policies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.springauthapi.authservice.resources.ResourceRequestProperties;
import com.springauthapi.authservice.resources.UserRequestProperties;

import jakarta.annotation.PostConstruct;
import java.lang.String;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PolicyService {

        @Value("${policy-service.base-url}")
        private String policyServiceBaseUrl;

        private WebClient webClient;

        private String resourceEndpointPath = "/api/check/resources";

        @PostConstruct
        public void init() {
                this.webClient = WebClient.builder()
                                .baseUrl(policyServiceBaseUrl)
                                .build();

                // System.out.println("✅ WebClient initialized with base URL: " +
                // policyServiceBaseUrl);
        }

        private Map<String, Object> buildCerbosRequest(
                        UserRequestProperties userRequestProperties,
                        ResourceRequestProperties resourceRequestProperties) {

                // could pass in an entity and get the type and id?
                Map<String, Object> principalMap = Map.of(
                                "id", userRequestProperties.getUserId(),
                                "roles", List.of(userRequestProperties.getRole().toLowerCase()));

                // could pass in an entity and get the type and id?
                Map<String, Object> resourceMap = new HashMap<>();
                resourceMap.put("kind", resourceRequestProperties.getResourceType());
                resourceMap.put("id", resourceRequestProperties.getResourceId());

                String resourceUserId = resourceRequestProperties.getResourceOwnerId();

                // add resource attributes if passed
                if (!resourceUserId.isEmpty()) {
                        resourceMap.put("attr", Map.of(
                                        "ownerId", resourceUserId));
                }

                var requestBody = Map.of(
                                "requestId", UUID.randomUUID().toString(),
                                "principal", principalMap,
                                "resources", List.of(Map.of(
                                                "resource", resourceMap,
                                                "actions", resourceRequestProperties.getActionTypes())));
                
                                                

                return requestBody;
        }

        public Map<String, String> extractActions(Map<String, Object> response) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

                if (results == null || results.isEmpty()) {
                        throw new IllegalStateException("No results found in policy response");
                }

                Map<String, Object> firstResult = results.get(0);
                return (Map<String, String>) firstResult.get("actions");
        }

        /**
         * Check authorization for a given principal, resource, and list of actions.
         */
        public Map<String, String> checkAccess(
                        UserRequestProperties userRequestProperties,
                        ResourceRequestProperties resourceRequestProperties) {

                var requestBody = buildCerbosRequest(userRequestProperties, resourceRequestProperties);
                

                Map<String, Object> response = webClient.post()
                                .uri(resourceEndpointPath)
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                })
                                .block();

                return extractActions(response);
        }
}
