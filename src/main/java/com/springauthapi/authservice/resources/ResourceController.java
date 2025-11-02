package com.springauthapi.authservice.resources;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import com.springauthapi.authservice.policies.PolicyServiceResponseDto;

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

    @PostMapping("/")
    public List<PolicyServiceResponseDto> checkUsersPermissions(
            HttpServletRequest request,
            @Valid @RequestBody CheckResourceRequest checkResearceRequest) {

        return researceService.checkUsersPermissions(request,
                checkResearceRequest.getResourceType(),
                checkResearceRequest.getSafeResourceId(),
                checkResearceRequest.getActionTypes());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}