package com.springauthapi.authservice.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping()
    public Home home() {
        return new Home("Hello World!");
    }
}