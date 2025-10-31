package com.springauthapi.authservice.resources;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.springauthapi.authservice.jwt.JwtService;
import com.springauthapi.authservice.policies.PolicyService;
import com.springauthapi.authservice.policies.PolicyServiceResponseDto;
import com.springauthapi.authservice.user.Role;
import com.springauthapi.authservice.utils.AuthHeaderUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ResearceService {

    private final String resourceAllowedValue = "EFFECT_ALLOW";

    private final JwtService jwtService;
    private final PolicyService policyService;

    public Map<String, String> checkAccessTest(
            String token,
            String resourceKind,
            String resourceId,
            List<String> actions) {

        var jwt = jwtService.parseToken(token);
        Long userId = jwt.getUserId();
        Role role = jwt.getRole();

        var response = policyService.checkAccess(
                userId.toString(),
                role.toString(),
                resourceKind,
                resourceId,
                actions);

        return response;
    }

    public String validateToken(HttpServletRequest request) {
        String token = AuthHeaderUtil.extractToken(request);

        Boolean isValid = jwtService.validateToken(token);
        if (!isValid) {
            throw new AccessDeniedException("Token not valid");
        }

        return token;
    }

    public String generateMessage(
            String resourceKind,
            String actionType,
            Boolean isAuthorised) {

        var isNotStrong = " not";
        if (isAuthorised) {
            isNotStrong = "";
        }

        return String.format("User is%s Authorized to %s the %s resource.",
                isNotStrong,
                actionType.toUpperCase(),
                resourceKind.toUpperCase());
    }

    public List<PolicyServiceResponseDto> mapToPolicyServiceResponse(Map<String, String> response) {
        return response.entrySet()
                .stream()
                .map(entry -> {
                    boolean isAllowed = entry.getValue().equals(resourceAllowedValue);
                    return new PolicyServiceResponseDto(entry.getKey(), isAllowed);
                })
                .toList();
    }

    /**
     * checkUsersPermissions
     * checks a user permissins of multiple action types against a resource
     * 
     * @param request
     * @param resourceType
     * @param resourceId
     * @param actions
     * @return
     */
    public List<PolicyServiceResponseDto> checkUsersPermissions(
            HttpServletRequest request,
            String resourceType,
            String resourceId,
            List<String> actions) {

        // throws exception id token is not valid
        String token = validateToken(request);

        var response = checkAccessTest(token, resourceType, resourceId, actions);

        return mapToPolicyServiceResponse(response);
    }

    /**
     * checkUsersPermission
     * checks a user permissins of a single action type against a resource
     * 
     * @param request
     * @param resourceKind
     * @param resourceId
     * @param actionType
     * @return
     */
    public PolicyServiceResponseDto checkUsersPermission(
            HttpServletRequest request,
            String resourceKind,
            String resourceId,
            String actionType) {

        // throws exception id token is not valid
        String token = validateToken(request);

        var response = checkAccessTest(token, resourceKind, resourceId, List.of(actionType));

        // could be simplified, but decided to reuse logic in mapToPolicyServiceResponse
        // should only be complexity on n(1), as only one action type should ever be returned
        PolicyServiceResponseDto policyMap = mapToPolicyServiceResponse(response).stream()
                .filter(p -> p.getAction().equals(actionType))
                .findFirst()
                .orElseThrow();

        var message = generateMessage(resourceKind, actionType, policyMap.getIsAuthorized());

        return new PolicyServiceResponseDto(message, policyMap.getIsAuthorized());
    }
}
