package com.springauthapi.authservice.resources;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceRequestProperties {
    private String resourceType;
    private String resourceId;
    private String resourceOwnerId;
    private List<String> actionTypes;
}