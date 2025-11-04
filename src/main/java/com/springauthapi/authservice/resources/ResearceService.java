package com.springauthapi.authservice.resources;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.springauthapi.authservice.jwt.JwtService;
import com.springauthapi.authservice.policies.PolicyConstants;
import com.springauthapi.authservice.policies.PolicyService;
import com.springauthapi.authservice.user.Role;
import com.springauthapi.authservice.utils.AuthHeaderUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ResearceService {

    private final JwtService jwtService;
    private final PolicyService policyService;

    public Map<String, String> checkAccess(
            String token,
            ResourceRequestProperties resourceRequestProperties) {

        var jwt = jwtService.parseToken(token);
        Long userId = jwt.getUserId();
        Role role = jwt.getRole();

        var userProperties = UserRequestProperties.builder()
                .role(role.toString())
                .userId(userId.toString())
                .build();

        var response = policyService.checkAccess(
                userProperties,
                resourceRequestProperties);

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

    public List<CheckResourceResDto> mapToPolicyServiceResponse(Map<String, String> response) {
        return response.entrySet()
                .stream()
                .map(entry -> {
                    boolean isAllowed = entry.getValue().equals(PolicyConstants.RESOURCE_EFFECT_ALLOW);
                    return new CheckResourceResDto(entry.getKey(), isAllowed);
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
    public List<CheckResourceResDto> checkUsersPermissions(
            HttpServletRequest request,
            ResourceRequestProperties resourceRequestProperties) {

        // throws exception id token is not valid
        String token = validateToken(request);

        var response = checkAccess(token, resourceRequestProperties);

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
    public CheckResourceResDto checkUsersActionAccess(
            HttpServletRequest request,
            ResourceRequestProperties resourceRequestProperties) {

        // throws exception id token is not valid
        String token = validateToken(request);

        var response = checkAccess(token, resourceRequestProperties);

        // checkUsersActionAccess is only meant to handle checking access of one action type
        // so we only expect one action type to be passed in, even though the property type is a List
        var actionType = resourceRequestProperties.getActionTypes().get(0);

        // could be simplified, but decided to reuse logic in mapToPolicyServiceResponse
        // should only be complexity on n(1), as only one action type should ever be
        // returned
        CheckResourceResDto policyMap = mapToPolicyServiceResponse(response).stream()
                .filter(p -> p.getAction().equals(actionType))
                .findFirst()
                .orElseThrow();

        var message = generateMessage(
                resourceRequestProperties.getResourceType(),
                actionType,
                policyMap.getIsAuthorized());

        return new CheckResourceResDto(message, policyMap.getIsAuthorized());
    }
}
