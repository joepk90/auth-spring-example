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
@Tag(name = "Resource Controller", description = "Controller for checking a user's permissions of a resource. To use thhe controlelr you must be authenticated.")
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
    public List<CheckResourceResponse> checkUsersPermissions(
            HttpServletRequest request,
            @Valid @RequestBody CheckResourceRequest checkResearceRequest) {

        return researceService.checkUsersPermissions(request,
                checkResearceRequest.getResourceType(),
                checkResearceRequest.getSafeResourceId(),
                checkResearceRequest.getActionTypes());
    }

    @GetMapping("/post/view")
    public CheckResourceResponse canView(HttpServletRequest request) {
        return researceService.checkUsersPermission(
                request,
                PolicyConstants.RESOURCE_POST,
                "*",
                PolicyConstants.ACTION_VIEW);
    }

    @GetMapping("/post/view/{id}")
    public CheckResourceResponse canViewWithId(
            HttpServletRequest request,
            @PathVariable String id) {

        return researceService.checkUsersPermission(
                request,
                PolicyConstants.RESOURCE_POST, id,
                PolicyConstants.ACTION_VIEW);
    }

    @GetMapping("/post/edit")
    public CheckResourceResponse canEdit(HttpServletRequest request) {
        return researceService.checkUsersPermission(
                request,
                PolicyConstants.RESOURCE_POST,
                "*",
                PolicyConstants.ACTION_EDIT);
    }

    @GetMapping("/post/edit/{id}")
    public CheckResourceResponse canEditWithId(
            HttpServletRequest request,
            @PathVariable String id) {

        return researceService.checkUsersPermission(
                request,
                PolicyConstants.RESOURCE_POST, id,
                PolicyConstants.ACTION_EDIT);
    }

    @GetMapping("/post/delete")
    public CheckResourceResponse canDelete(HttpServletRequest request) {
        return researceService.checkUsersPermission(
                request,
                PolicyConstants.RESOURCE_POST,
                "*",
                PolicyConstants.ACTION_DELETE);
    }

    @GetMapping("/post/delete/{id}")
    public CheckResourceResponse canDeleteWithId(
            HttpServletRequest request,
            @PathVariable String id) {

        return researceService.checkUsersPermission(
                request,
                PolicyConstants.RESOURCE_POST, id,
                PolicyConstants.ACTION_DELETE);
    }

    @GetMapping("/post/create")
    public CheckResourceResponse canCreate(HttpServletRequest request) {
        return researceService.checkUsersPermission(
                request,
                PolicyConstants.RESOURCE_POST,
                "*",
                PolicyConstants.ACTION_CREATE);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}