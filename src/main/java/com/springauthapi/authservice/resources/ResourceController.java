package com.springauthapi.authservice.resources;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import com.springauthapi.authservice.policies.PolicyConstants;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Resource Controller", description = "Controller for checking a user's permissions of a resource. To use the controller you must be authenticated.")
@RequestMapping("/resource")
public class ResourceController {

    private final ResearceService researceService;

    /**
     * Root Endpoint (POST):
     * Used to check multiple action types, and a resource ID, against a resource
     * 
     * @param request
     * @param checkResearceRequest
     * @return
     */
    @PostMapping("/")
    public List<CheckResourceResDto> checkUsersPermissions(
            HttpServletRequest request,
            @Valid @RequestBody CheckResourceReqDto checkResearceDto) {

        var resourceProperties = ResourceRequestProperties.builder()
                .resourceType(checkResearceDto.getResourceType())
                .resourceId(checkResearceDto.getResourceId())
                .actionTypes(checkResearceDto.getActionTypes())
                .resourceOwnerId(checkResearceDto.getResourceOwnerId())
                .build();

        return researceService.checkUsersPermissions(request, resourceProperties);
    }

    @PostMapping("/post/view")
    public CheckResourceResDto canView(HttpServletRequest request,
            @Valid @RequestBody CheckResourceActionReqDto checkResearceRecordRequest) {
        var resourceProperties = buildResourcePropertiesWithSinglularAction(
                PolicyConstants.RESOURCE_ID,
                PolicyConstants.ACTION_VIEW,
                checkResearceRecordRequest.getResourceOwnerId());

        return researceService.checkUsersActionAccess(
                request,
                resourceProperties);
    }



    @PostMapping("/post/edit")
    public CheckResourceResDto canEdit(HttpServletRequest request,
            @Valid @RequestBody CheckResourceActionReqDto checkResearceRecordRequest) {
        var resourceProperties = buildResourcePropertiesWithSinglularAction(
                PolicyConstants.RESOURCE_ID,
                PolicyConstants.ACTION_EDIT,
                checkResearceRecordRequest.getResourceOwnerId());

        return researceService.checkUsersActionAccess(
                request,
                resourceProperties);
    }

    @PostMapping("/post/delete")
    public CheckResourceResDto canDelete(HttpServletRequest request,
            @Valid @RequestBody CheckResourceActionReqDto checkResearceRecordRequest) {
        var resourceProperties = buildResourcePropertiesWithSinglularAction(
                PolicyConstants.RESOURCE_ID,
                PolicyConstants.ACTION_DELETE,
                checkResearceRecordRequest.getResourceOwnerId());

        return researceService.checkUsersActionAccess(
                request,
                resourceProperties);
    }

    @PostMapping("/post/create")
    public CheckResourceResDto canCreate(HttpServletRequest request,
            @Valid @RequestBody CheckResourceActionReqDto checkResearceRecordRequest) {
        var resourceProperties = buildResourcePropertiesWithSinglularAction(
                PolicyConstants.RESOURCE_ID,
                PolicyConstants.ACTION_CREATE,
                checkResearceRecordRequest.getResourceOwnerId());

        return researceService.checkUsersActionAccess(
                request,
                resourceProperties);
    }

    public ResourceRequestProperties buildResourcePropertiesWithSinglularAction(String resourceId, String actionType,
            String ownerId) {
        return ResourceRequestProperties.builder()
                .resourceType(PolicyConstants.RESOURCE_POST)
                .resourceId(resourceId)
                .actionTypes(List.of(actionType))
                .resourceOwnerId(ownerId)
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}