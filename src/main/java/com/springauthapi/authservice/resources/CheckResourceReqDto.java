package com.springauthapi.authservice.resources;

import lombok.Builder;
import lombok.Data;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
@Builder
public class CheckResourceReqDto {
    @NotBlank(message = "Resource Type is required")
    private String resourceType;

    @NotEmpty(message = "Action Types cannot be empty")
    @Size(min = 1, message = "At least one action type must be provided")
    private List<String> actionTypes;


    private String resourceId;
    private String resourceOwnerId;
}