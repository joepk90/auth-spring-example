package com.springauthapi.authservice.resources;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import com.springauthapi.authservice.policies.PolicyServiceResponseDto;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/resource-post")
public class ResourcePostController {

    private final String reseourceType = "post"; // could use entity type?

    private final ResearceService researceService;

    @GetMapping("/view")
    public PolicyServiceResponseDto canView(HttpServletRequest request) {
        return researceService.checkUsersPermission(request, reseourceType, "*", "view");
    }

    @GetMapping("/view/{id}")
    public PolicyServiceResponseDto canViewWithId(
            HttpServletRequest request,
            @PathVariable String id) {

        return researceService.checkUsersPermission(request, reseourceType, id, "view");
    }

    @GetMapping("/edit")
    public PolicyServiceResponseDto canEdit(HttpServletRequest request) {
        return researceService.checkUsersPermission(request, reseourceType, "*", "edit");
    }

    @GetMapping("/edit/{id}")
    public PolicyServiceResponseDto canEditWithId(
            HttpServletRequest request,
            @PathVariable String id) {

        return researceService.checkUsersPermission(request, reseourceType, id, "edit");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
