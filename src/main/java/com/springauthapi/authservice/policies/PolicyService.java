package com.springauthapi.authservice.policies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import java.lang.String;

import java.util.List;
import java.util.Map;

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
                        String principal,
                        String role,
                        String resourceKind,
                        String resourceId,
                        List<String> actions) {

                // could pass in an entity and get the type and id?
                Map<String, Object> principalMap = Map.of(
                                "id", principal,
                                "roles", List.of(role.toLowerCase()));

                // could pass in an entity and get the type and id?
                Map<String, Object> resourceMap = Map.of(
                                "kind", resourceKind,
                                "id", resourceId);

                var requestBody = Map.of(
                                // "requestId", UUID.randomUUID().toString(),
                                "principal", principalMap,
                                "resources", List.of(Map.of(
                                                "resource", resourceMap,
                                                "actions", actions)));

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
        public Map<String, String> checkAccess(String principle, String role, String resourceKind, String resourceId,
                        List<String> actions) {

                var requestBody = buildCerbosRequest(principle, role, resourceKind, resourceId, actions);

                Map<String, Object> response = webClient.post()
                                .uri(resourceEndpointPath)
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                                .block();

                return extractActions(response);
        }
}
