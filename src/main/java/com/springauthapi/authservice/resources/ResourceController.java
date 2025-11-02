package com.springauthapi.authservice.resources;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Resource Controller")
@RequestMapping("/resource")
public class ResourceController {

    private final ResearceService researceService;
    private final String reseourceType = "post"; // could use entity type?


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
        return researceService.checkUsersPermission(request, reseourceType, "*", "view");
    }

    @GetMapping("/post/view/{id}")
    public CheckResourceResponse canViewWithId(
            HttpServletRequest request,
            @PathVariable String id) {

        return researceService.checkUsersPermission(request, reseourceType, id, "view");
    }

    @GetMapping("/post/edit")
    public CheckResourceResponse canEdit(HttpServletRequest request) {
        return researceService.checkUsersPermission(request, reseourceType, "*", "edit");
    }

    @GetMapping("/post/edit/{id}")
    public CheckResourceResponse canEditWithId(
            HttpServletRequest request,
            @PathVariable String id) {

        return researceService.checkUsersPermission(request, reseourceType, id, "edit");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}