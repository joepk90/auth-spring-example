package com.springauthapi.authservice.resources;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckResourceRequest {
    @NotBlank(message = "Resource Type is required")
    private String resourceType;

    // @NotNull(message = "Action Types are required")
    @NotEmpty(message = "Action Types cannot be empty")
    @Size(min = 1, message = "At least one action type must be provided")
    private List<String> actionTypes;


    private String resourceId;

    @JsonIgnore
    public String getSafeResourceId() {
        return (resourceId == null || resourceId.isBlank()) ? "*" : resourceId;
    }
}
