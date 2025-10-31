package com.springauthapi.authservice.resources;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import com.springauthapi.authservice.jwt.JwtService;
import com.springauthapi.authservice.policies.PolicyServiceResponseDto;
import com.springauthapi.authservice.policies.PolicyService;
import com.springauthapi.authservice.user.Role;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    
}
